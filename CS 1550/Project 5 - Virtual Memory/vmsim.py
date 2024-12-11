import random
import sys
import argparse
import re

class PageTableEntry:
    def __init__(self):
        self.valid = False
        self.dirty = False
        self.referenced = False
        self.second_level = None  # For root entries only
        self.virtual_addresses= []  # For leaf entries only
        self.timestamp = None
        
def get_page_table_indices(address):
    addr = int(address, 16)
    
    # Extract the indices using bit operations
    second_level_index = (addr >> 11) & 0x3FF  # Middle 10 bits
    root_index = (addr >> 21) & 0x7FF  # Upper 11 bits

    return root_index, second_level_index

# This function reads the trace file and returns a list of page accesses for OPT
# Returns a dictionary that is in the format {page number: [index, index, index, ...]}
def readTraceFile(trace):
    page_accesses = {}
    current_index = 0
    for line in trace:
        stripped_line = line.lstrip()
        if not stripped_line.startswith(('I', 'S', 'M', 'L')):
            continue
        split_line = re.split(r'[ ,]+', stripped_line.strip())
        hex_memory_address = split_line[1]
        virtual_address = int(hex_memory_address, 16)
        page_number = virtual_address >> 11
        if page_number not in page_accesses:
            page_accesses[page_number] = []
        page_accesses[page_number].append(current_index)
        current_index += 1
    return page_accesses

# This function runs the OPT algorithm on the trace file. 
# Reads future memory accesses and replaces the page that will not be used for the longest time
def OPT(trace, num_frames):
    total_memory_accesses = 0
    total_page_faults = 0
    total_writes = 0
    number_of_leaves = 0
    
    # Initialize the page table
    root_page_table = [PageTableEntry() for _ in range(2048)]
    
    # Initialize the frames and free frames
    frames = [PageTableEntry() for _ in range(num_frames)]    
    free_frames = list(range(num_frames))
    
    # Read the trace file and get the instructions    
    pageAccesses = readTraceFile(trace)
    
    # Seek back because we already read the file in readTraceFile
    trace.seek(0)
    for tracefile_index, line in enumerate(trace):
        stripped_line = line.lstrip()  # Remove leading spaces
        if not stripped_line.startswith(('I', 'S', 'M', 'L')):
            continue
        
        split_line = re.split(r'[ ,]+', stripped_line.strip())
        access_instruction = split_line[0]
        memory_address = split_line[1]
        
        total_memory_accesses += 1
        
        # Get the indices for the page table
        root_idx, second_idx = get_page_table_indices(memory_address)
        
        # If the address references a leaf that is not created, we need to update the page table by creating the leaf
        # The leaf is created with 1024 PageTableEntries objects as per the project instructions
        if not root_page_table[root_idx].second_level:
            root_page_table[root_idx].second_level = [PageTableEntry() for _ in range(1024)]
            number_of_leaves += 1
        
        # Now that the leaf node is for sure created, we can get the page table entry at the second level index
        second_level = root_page_table[root_idx].second_level
        page_entry = second_level[second_idx]
        page_entry.virtual_addresses.append(memory_address)
        
        # Page hit. No need to do evict anything or update the page table. Move on to the next instruction
        if page_entry.valid:
            if access_instruction in ['S', 'M']:
                page_entry.dirty = True
            continue
        
        # Page fault. We need to evict a page
        total_page_faults += 1
        
        # If there are free frames, we can use one of them
        if free_frames:
            frame_number = free_frames.pop(0)
            page_entry.valid = True
            page_entry.referenced = True
            if access_instruction in ['S', 'M']:
                page_entry.dirty = True
            frames[frame_number] = page_entry

        else:
            # Need to evict a page using the OPT algorithm
            # Find the page that won't be used for the longest time
            frame_to_evict = (-1,-1)
            for frame_num, pte in enumerate(frames):
                candidate = (-1,-1)
                addr = int(pte.virtual_addresses[0], 16)
                page_number = addr >> 11
                for future_index in pageAccesses[page_number]:
                    if future_index > tracefile_index:
                        candidate = (frame_num, future_index)
                        break
                if candidate == (-1,-1):  # If the Page won't be used again, set its index to infinity
                    candidate = (frame_num, float('inf'))  
                if candidate[1] > frame_to_evict[1]:    # If the candidate is used later than the current frame to evict, update the frame to evict
                    frame_to_evict = candidate
                elif candidate[1] == frame_to_evict[1] and not pte.dirty and frames[frame_to_evict[0]].dirty:   # Opt prefers clean frames, so if both won't be used again, pick the clean one if possible
                    frame_to_evict = candidate
            
            #Unpack to tuple to get the frame number and make it easier to use        
            frame_to_evict = frame_to_evict[0]
                
            # Evict the page
            if frames[frame_to_evict].dirty:
                total_writes += 1
            frames[frame_to_evict].valid = False
            frames[frame_to_evict].dirty = False
            frames[frame_to_evict].referenced = False
            
            # Update the page table
            page_entry.valid = True
            page_entry.referenced = frame_to_evict
            frames[frame_to_evict] = page_entry
            if access_instruction in ['S', 'M']:
                page_entry.dirty = True
            
            
            
    # Calculate total size of page table (in bytes). Each page is 2KB and there are 2048 pages in the root table and 1024 pages in each leaf table
    total_page_table_size = (2048 * 2048) + (number_of_leaves * 1024 * 2048)  

    # Print statistics
    print("\nAlgorithm: OPT")
    print(f"Number of frames: {num_frames}")
    print(f"Total memory accesses: {total_memory_accesses}")
    print(f"Total page faults: {total_page_faults}")
    print(f"Total writes to disk: {total_writes}")
    print(f"Number of page table leaves: {number_of_leaves}")
    print(f"Total size of page table: {total_page_table_size}")


## This function runs the RAND algorithm on the trace file. It randomly selects a page to evict --------------------------    
def RAND(trace, num_frames):
    total_memory_accesses = 0
    total_page_faults = 0
    total_writes = 0
    number_of_leaves = 0
    
    root_page_table = [PageTableEntry() for _ in range(2048)]
    
    # Initialize the frames and free frames. 'frames' is just a list of PageTableEntry objects because it is way easier instead of making a frame object. 
    frames = [PageTableEntry() for _ in range(num_frames)]
    free_frames = list(range(num_frames))
    
    # Read the trace file and get the instructions
    for line in trace:
        stripped_line = line.lstrip()  # Remove leading spaces
        if not stripped_line.startswith(('I', 'S', 'M', 'L')):
            continue
        
        split_line = re.split(r'[ ,]+', stripped_line.strip())
        access_instruction = split_line[0]
        memory_address = split_line[1]
        
        total_memory_accesses += 1
        
        # Get the indices for the page table
        root_idx, second_idx = get_page_table_indices(memory_address)
        
        # If the address references a leaf node that is not valid, we need to update the page table
        # The leaf is created with 1024 PageTableEntries objects
        if not root_page_table[root_idx].second_level:
            root_page_table[root_idx].second_level = [PageTableEntry() for _ in range(1024)]
            number_of_leaves += 1
        
        # Now that the leaf node is for sure created, we can get the page entry
        second_level = root_page_table[root_idx].second_level
        page_entry = second_level[second_idx]
        
        # Page hit. No need to do evict anything or update the page table. Move on to the next instruction
        if page_entry.valid:
            if access_instruction in ['S', 'M']:
                page_entry.dirty = True
            continue
        else: 
            total_page_faults += 1
            if free_frames:  # If there are free frames, we can use one of them 
                frame_number = free_frames.pop(0)
                page_entry.valid = True
                page_entry.reference = True                
                if access_instruction in ['S', 'M']:
                    page_entry.dirty = True       
                frames[frame_number] = page_entry        
            else: # Randomly select a page to evict and replace it with the new page
                frame_number = random.randint(0, num_frames - 1)
                if frames[frame_number].dirty:
                    total_writes += 1
                frames[frame_number].dirty = False
                frames[frame_number].valid = False
                frames[frame_number].referenced = False
                page_entry.valid = True
                page_entry.reference = True
                if access_instruction in ['S', 'M']:
                    page_entry.dirty = True
                frames[frame_number] = page_entry
    
    total_page_table_size = (2048 * 2048) + (number_of_leaves * 1024 * 2048)  

    # Print statistics
    print("\nAlgorithm: RAND")
    print(f"Number of frames: {num_frames}")
    print(f"Total memory accesses: {total_memory_accesses}")
    print(f"Total page faults: {total_page_faults}")
    print(f"Total writes to disk: {total_writes}")
    print(f"Number of page table leaves: {number_of_leaves}")
    print(f"Total size of page table: {total_page_table_size}")

## This function runs the second change clock algorithm on the trace file        
def CLOCK(trace, num_frames):
    total_memory_accesses = 0
    total_page_faults = 0
    total_writes = 0
    number_of_leaves = 0
    
    root_page_table = [PageTableEntry() for _ in range(2048)]
    
    # Initialize the frames and free frames. 'frames' is just a list of PageTableEntry objects because it is way easier instead of making a frame object. 
    frames = [PageTableEntry() for _ in range(num_frames)]
    free_frames = list(range(num_frames))
    
    clock_hand = 0
    
    # Read the trace file and get the instructions
    for line in trace:
        stripped_line = line.lstrip()  # Remove leading spaces
        if not stripped_line.startswith(('I', 'S', 'M', 'L')):
            continue
        
        split_line = re.split(r'[ ,]+', stripped_line.strip())
        access_instruction = split_line[0]
        memory_address = split_line[1]
        
        total_memory_accesses += 1
        
        # Get the indices for the page table
        root_idx, second_idx = get_page_table_indices(memory_address)
        
        # If the address references a leaf node that is not valid, we need to update the page table
        # The leaf is created with 1024 PageTableEntries objects
        if not root_page_table[root_idx].second_level:
            root_page_table[root_idx].second_level = [PageTableEntry() for _ in range(1024)]
            number_of_leaves += 1
        
        # Now that the leaf node is for sure created, we can get the page entry
        second_level = root_page_table[root_idx].second_level
        page_entry = second_level[second_idx]
        
        # Page hit. No need to do evict anything or update the page table. Move on to the next instruction
        if page_entry.valid:
            page_entry.referenced = True
            if access_instruction in ['S', 'M']:
                page_entry.dirty = True
            continue
        else: 
            total_page_faults += 1
            if free_frames:  # If there are free frames, we can use one of them 
                frame_number = free_frames.pop(0)
                page_entry.valid = True
                page_entry.reference = True                
                if access_instruction in ['S', 'M']:
                    page_entry.dirty = True
                frames[frame_number] = page_entry
            else: # If the current frame is referenced, set the reference bit to false and move to the next frame until we find a frame that is not referenced. Evict that frame
                while(True):
                    if frames[clock_hand].referenced:
                        frames[clock_hand].referenced = False
                        clock_hand = (clock_hand + 1) % num_frames
                    else:
                        if frames[clock_hand].dirty:
                            total_writes += 1
                        frames[clock_hand].dirty = False
                        frames[clock_hand].valid = False
                        frames[clock_hand].referenced = False
                        
                        page_entry.valid = True
                        page_entry.reference = True
                        if access_instruction in ['S', 'M']:
                            page_entry.dirty = True
                        frames[clock_hand] = page_entry
                        clock_hand = (clock_hand + 1) % num_frames
                        break
    
    ##Print Stats
    total_page_table_size = (2048 * 2048) + (number_of_leaves * 1024 * 2048)  

    # Print statistics
    print("\nAlgorithm: CLOCK")
    print(f"Number of frames: {num_frames}")
    print(f"Total memory accesses: {total_memory_accesses}")
    print(f"Total page faults: {total_page_faults}")
    print(f"Total writes to disk: {total_writes}")
    print(f"Number of page table leaves: {number_of_leaves}")
    print(f"Total size of page table: {total_page_table_size}")


## This function runs the LRU algorithm on the trace file. It evicts the page that has not been used for the longest time
def LRU(trace, num_frames):
    total_memory_accesses = 0
    total_page_faults = 0
    total_writes = 0
    number_of_leaves = 0
    
    root_page_table = [PageTableEntry() for _ in range(2048)]
    
    # Initialize the frames and free frames. 'frames' is just a list of PageTableEntry objects because it is way easier instead of making a frame object. 
    frames = [PageTableEntry() for _ in range(num_frames)]
    free_frames = list(range(num_frames))
        
    # Read the trace file and get the instructions
    trace.seek(0)
    for time, line in enumerate(trace):
        stripped_line = line.lstrip()  # Remove leading spaces
        if not stripped_line.startswith(('I', 'S', 'M', 'L')):
            continue
        
        split_line = re.split(r'[ ,]+', stripped_line.strip())
        access_instruction = split_line[0]
        memory_address = split_line[1]
        
        total_memory_accesses += 1
        
        # Get the indices for the page table
        root_idx, second_idx = get_page_table_indices(memory_address)
        
        # If the address references a leaf node that is not valid, we need to update the page table
        # The leaf is created with 1024 PageTableEntries objects
        if not root_page_table[root_idx].second_level:
            root_page_table[root_idx].second_level = [PageTableEntry() for _ in range(1024)]
            number_of_leaves += 1
        
        # Now that the leaf node is for sure created, we can get the page entry
        second_level = root_page_table[root_idx].second_level
        page_entry = second_level[second_idx]
        
        # Page hit. No need to do evict anything or update the page table. Move on to the next instruction
        if page_entry.valid:
            if access_instruction in ['S', 'M']:
                page_entry.dirty = True
            page_entry.timestamp = time
            continue
        else: 
            total_page_faults += 1
            if free_frames:  # If there are free frames, we can use one of them 
                frame_number = free_frames.pop(0)
                page_entry.valid = True
                page_entry.reference = True                
                if access_instruction in ['S', 'M']:
                    page_entry.dirty = True
                page_entry.timestamp = time
                frames[frame_number] = page_entry
            else: 
                frame_to_evict = min(range(num_frames), key=lambda x: frames[x].timestamp)
                if frames[frame_to_evict].dirty:
                    total_writes += 1
                frames[frame_to_evict].dirty = False
                frames[frame_to_evict].valid = False
                frames[frame_to_evict].referenced = False
                page_entry.valid = True
                page_entry.reference = True
                if access_instruction in ['S', 'M']:
                    page_entry.dirty = True
                page_entry.timestamp = time
                frames[frame_to_evict] = page_entry
                
    ##Print Stats
    total_page_table_size = (2048 * 2048) + (number_of_leaves * 1024 * 2048)  

    # Print statistics
    print("\nAlgorithm: LRU")
    print(f"Number of frames: {num_frames}")
    print(f"Total memory accesses: {total_memory_accesses}")
    print(f"Total page faults: {total_page_faults}")
    print(f"Total writes to disk: {total_writes}")
    print(f"Number of page table leaves: {number_of_leaves}")
    print(f"Total size of page table: {total_page_table_size}")    
                    
                
    

## This function parses the arguments passed to the program and provides an error message if the arguments are not valid
def parse_arguments():
    parser = argparse.ArgumentParser(description='Virtual Memory Simulator')
    
    # Set up all the required args
    parser.add_argument('-n', type=int, required=True,
                      help='Number of frames')
    parser.add_argument('-a', choices=['opt', 'rand', 'clock', 'lru'],
                      required=True, help='Virtual Memory Algorithm to use')
    parser.add_argument('tracefile', help='Path to the trace file')
    
    return parser.parse_args()

## Main function that runs the program
def main():
    args = parse_arguments()
    
    try: 
        trace = open(args.tracefile, 'r')
    except FileNotFoundError:
        print('File not found')
        sys.exit(1)
        
    num_frames = int(args.n)
    algorithm = args.a
    
    if algorithm == 'opt':
        OPT(trace, num_frames)
    elif algorithm == 'rand':
        RAND(trace, num_frames)
    elif algorithm == 'clock':
        CLOCK(trace, num_frames)
    elif algorithm == 'lru':
        LRU(trace, num_frames)
    else:
        print('Invalid algorithm')
        sys.exit(1)

if __name__ == '__main__':
    main()