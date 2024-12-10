import random
import sys
import argparse
import re

class PageTableEntry:
    def __init__(self):
        self.valid = False
        self.dirty = False
        self.referenced = False
        self.frame_number = None
        self.second_level = None  # For root entries only

class Frame:
    def __init__(self):
        self.page_number = None
        self.dirty = False
        self.last_access = 0
        
def get_page_table_indices(address):
    # Convert hex address to integer if it's a string
    addr = int(address, 16)
    
    # Extract the indices using bit operations
    second_level_index = (addr >> 11) & 0x3FF  # Middle 10 bits
    root_index = (addr >> 21) & 0x7FF  # Upper 11 bits
    
    return root_index, second_level_index

# This function reads the trace file and returns a list of instructions for OPT
# Returns a dictionary that is in the format {memory_address: [(access_instruction, index)]}
def readTraceFile(trace):
    instructions = {}
    for index, line in enumerate(trace):
        stripped_line = line.lstrip()  # Remove leading spaces
        if stripped_line.startswith(('I', 'S', 'M', 'L')):
            split_line = re.split(r'[ ,]+', stripped_line.strip())
            access_instruction = split_line[0]
            memory_address = split_line[1]
            if memory_address not in instructions:
                instructions[memory_address] = []
            instructions[memory_address].append((access_instruction, index))
    return instructions 

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
    frames = [Frame() for _ in range(num_frames)]
    free_frames = list(range(num_frames))

    # Read the trace file and get the instructions    
    fileInstructions = readTraceFile(trace)
    
    trace.seek(0)
    for index, line in enumerate(trace):
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
                frames[page_entry.frame_number].dirty = True
            continue
        
        # Page fault. We need to evict a page and update the page table
        total_page_faults += 1
        
        # If there are free frames, we can use one of them
        if free_frames:
            frame_number = free_frames.pop(0)
        else:
            # Need to evict a page using the OPT algorithm
            # Find the page that won't be used for the longest time
            max_future_distance = -1
            frame_to_evict = 0
            
            for i, frame in enumerate(frames):
                # Find next use of this page
                future_distance = float('inf')
                
            ##TODO: The rest of the OPT algorithm 
            

    # Calculate total size of page table (in bytes)
    total_page_table_size = (2048 * 4) + (number_of_leaves * 1024 * 4)  # Assuming 4 bytes per entry

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
    
    # Initialize the frames and free frames
    frames = [Frame() for _ in range(num_frames)]
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
                frames[page_entry.frame_number].dirty = True
            continue
        else: 
            total_page_faults += 1
            if free_frames:
                frame_number = free_frames.pop(0)
            else:
                frame_number = random.choice(range(num_frames))
                if frames[frame_number].dirty:
                    total_writes += 1
                frames[frame_number].page_number = None
                frames[frame_number].dirty = False
            page_entry.valid = True
            page_entry.frame_number = frame_number
            frames[frame_number].page_number = memory_address
            if access_instruction in ['S', 'M']:
                page_entry.dirty = True
                frames[frame_number].dirty = True
                
    total_page_table_size = (2048 * 4) + (number_of_leaves * 1024 * 4)  # Assuming 4 bytes per entry

    # Print statistics
    print("\nAlgorithm: RAND")
    print(f"Number of frames: {num_frames}")
    print(f"Total memory accesses: {total_memory_accesses}")
    print(f"Total page faults: {total_page_faults}")
    print(f"Total writes to disk: {total_writes}")
    print(f"Number of page table leaves: {number_of_leaves}")
    print(f"Total size of page table: {total_page_table_size}")

    
def CLOCK(trace, num_frames):
    total_memory_accesses = 0
    total_page_faults = 0
    total_writes = 0
    number_of_leaves = 0
    page_table = {}


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
        print('Running OPT')
        OPT(trace, num_frames)
    elif algorithm == 'rand':
        print('Running RAND')
        RAND(trace, num_frames)
    elif algorithm == 'clock':
        print('Running CLOCK')
    elif algorithm == 'lru':
        print('Running LRU')
    else:
        print('Invalid algorithm')
        sys.exit(1)

if __name__ == '__main__':
    main()