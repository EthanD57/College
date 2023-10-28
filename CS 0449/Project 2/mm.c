// This adds coalescing of free blocks.
// Improves performance to 54/100 ... takes less time.

/*-------------------------------------------------------------------
 *  Malloc Lab Starter code:
 *        single doubly-linked free block list with LIFO policy
 *        with support for coalescing adjacent free blocks
 *
 * Terminology:
 * o We will implement an explicit free list allocator.
 * o We use "next" and "previous" to refer to blocks as ordered in
 *   the free list.
 * o We use "following" and "preceding" to refer to adjacent blocks
 *   in memory.
 *-------------------------------------------------------------------- */

#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <unistd.h>

#include "memlib.h"
#include "mm.h"

/* Macros for unscaled pointer arithmetic to keep other code cleaner.
   Casting to a char* has the effect that pointer arithmetic happens at
   the byte granularity (i.e. POINTER_ADD(0x1, 1) would be 0x2).  (By
   default, incrementing a pointer in C has the effect of incrementing
   it by the size of the type to which it points (e.g. Block).)
   We cast the result to void* to force you to cast back to the
   appropriate type and ensure you don't accidentally use the resulting
   pointer as a char* implicitly.
*/
#define UNSCALED_POINTER_ADD(p, x) ((void*)((char*)(p) + (x)))
#define UNSCALED_POINTER_SUB(p, x) ((void*)((char*)(p) - (x)))


/******** FREE LIST IMPLEMENTATION ***********************************/


/* An BlockInfo contains information about a block, including the size
   as well as pointers to the next and previous blocks in the free list.
   This is similar to the "explicit free list" structure illustrated in
   the lecture slides.

   Note that the next pointer are only needed when the block is free. To
   achieve better utilization, mm_malloc should use the space for next as
   part of the space it returns.

   +--------------+
   |     size     |  <-  Block pointers in free list point here
   |              |
   |   (header)   |
   |              |
   |     prev     |
   +--------------+
   |   nextFree   |  <-  Pointers returned by mm_malloc point here
   |   prevFree   |
   +--------------+      (allocated blocks do not have a 'nextFree' field)
   |  space and   |      (this is a space optimization...)
   |   padding    |
   |     ...      |      Free blocks write their nextFree/prevFree pointers in
   |     ...      |      this space.
   +--------------+

*/
typedef struct _BlockInfo {
  // Size of the block and whether or not the block is in use or free.
  // When the size is negative, the block is currently free.
  long int size;
  // Pointer to the previous block in the list.
  struct _Block* prev;
} BlockInfo;

/* A FreeBlockInfo structure contains metadata just for free blocks.
 * When you are ready, you can improve your naive implementation by
 * using these to maintain a separate list of free blocks.
 *
 * These are "kept" in the region of memory that is normally used by
 * the program when the block is allocated. That is, since that space
 * is free anyway, we can make good use of it to improve our malloc.
 */
typedef struct _FreeBlockInfo {
  // Pointer to the next free block in the list.
  struct _Block* nextFree;
  // Pointer to the previous free block in the list.
  struct _Block* prevFree;
} FreeBlockInfo;

/* This is a structure that can serve as all kinds of nodes.
 */
typedef struct _Block {
  BlockInfo info;
  FreeBlockInfo freeNode;
} Block;

/* Pointer to the first FreeBlockInfo in the free list, the list's head. */
static Block* free_list_head = NULL;
static Block* malloc_list_tail = NULL;

static size_t heap_size = 0;

/* Size of a word on this architecture. */
#define WORD_SIZE sizeof(void*)

/* Alignment of blocks returned by mm_malloc.
 * (We need each allocation to at least be big enough for the free space
 * metadata... so let's just align by that.)  */
#define ALIGNMENT (sizeof(FreeBlockInfo))

/* This function will have the OS allocate more space for our heap.
 *
 * It returns a pointer to that new space. That pointer will always be
 * larger than the last request and be continuous in memory.
 */
void* requestMoreSpace(size_t reqSize);

/* This function will get the first block or returns NULL if there is not
 * one.
 *
 * You can use this to start your through search for a block.
 */
Block* first_block();

/* This function will get the adjacent block or returns NULL if there is not
 * one.
 *
 * You can use this to move along your malloc list one block at a time.
 */
Block* next_block(Block* block);

/* Use this function to print a thorough listing of your heap data structures.
 */
void examine_heap();

/* Checks the heap for any issues and prints out errors as it finds them.
 *
 * Use this when you are debugging to check for consistency issues. */
int check_heap();

void coalesce(Block* blockInfo);

void free_List_Manager(Block* currentBlock, Block* otherblock, int Flag){   
    if (Flag == 1){ //After Split

        if (currentBlock != free_list_head){
            currentBlock -> freeNode.prevFree -> freeNode.nextFree = otherblock;   //The previous free block before current block has it's next free pointer set to the new block
            otherblock -> freeNode.prevFree = currentBlock -> freeNode.prevFree;    //The new block's previous free pointer is set to the previous free block before current block

        }
        else{
            free_list_head = otherblock;
            otherblock -> freeNode.prevFree = NULL;
        }
        if (currentBlock -> freeNode.nextFree != NULL){
            currentBlock -> freeNode.nextFree -> freeNode.prevFree = otherblock;
            otherblock -> freeNode.nextFree = currentBlock -> freeNode.nextFree;
        }
        else{
            otherblock -> freeNode.nextFree = NULL;
        }
    }
    else if (Flag == 2){ //No split
        if (free_list_head == currentBlock) {
            if (currentBlock -> freeNode.nextFree != NULL){
                free_list_head = currentBlock -> freeNode.nextFree;
                free_list_head -> freeNode.prevFree = NULL;
            }
            else free_list_head = NULL;
        }
        else {
            if (currentBlock->freeNode.prevFree != NULL && currentBlock->freeNode.nextFree != NULL) { //If we have a previous and next block that are free
                currentBlock->freeNode.prevFree->freeNode.nextFree = currentBlock->freeNode.nextFree;   //Set the next block of the previous block to the next block of the current block
                currentBlock->freeNode.nextFree->freeNode.prevFree = currentBlock->freeNode.prevFree;   //Set the previous block of the next block to the previous block of the current block
            }
            else if (currentBlock->freeNode.prevFree != NULL && currentBlock -> freeNode.nextFree == NULL){ //If we have a previous block that is free but no next block
                currentBlock->freeNode.prevFree->freeNode.nextFree = NULL;  //Set the next block of the previous block to NULL
            }
            else if (currentBlock->freeNode.prevFree == NULL && currentBlock -> freeNode.nextFree != NULL){ //If we have a next block that is free but no previous block
                currentBlock->freeNode.nextFree->freeNode.prevFree = NULL;  //Set the previous block of the next block to NULL
            }
        }
    }
    else if (Flag == 3){ //Freeing
        if (free_list_head == NULL) {
            free_list_head = currentBlock;
            currentBlock->freeNode.nextFree = NULL;
            currentBlock->freeNode.prevFree = NULL;
    }
        else { //Add the freed block into the list in the right order
            Block* previous = NULL;
            Block* current = free_list_head;
            if (currentBlock < free_list_head) {
                free_list_head -> freeNode.prevFree = currentBlock;
                currentBlock -> freeNode.nextFree = free_list_head;
                free_list_head = currentBlock;
                currentBlock -> freeNode.prevFree = NULL;
                return;
            }
            while (current != NULL && current < currentBlock) {
                previous = current;
                current = current->freeNode.nextFree;
            }
            previous -> freeNode.nextFree = currentBlock;
            currentBlock -> freeNode.nextFree = current;
            if (current != NULL) current -> freeNode.prevFree = currentBlock;
            currentBlock -> freeNode.prevFree = previous;
        }
    }
//COALLESCE -----------------------------------------------------------------------------
    else if (Flag == 4) //Coallescing 3 blocks together 
    {
        if (otherblock->freeNode.nextFree != NULL) {
            otherblock->freeNode.nextFree->freeNode.prevFree = currentBlock;
            currentBlock->freeNode.nextFree = otherblock->freeNode.nextFree;
        }
        else {
            currentBlock->freeNode.nextFree = NULL;
        }
    }
    else if (Flag == 5){ //Coallescing current block to the block after it
        if (otherblock->freeNode.nextFree != NULL) {
            otherblock->freeNode.nextFree->freeNode.prevFree = currentBlock;
            currentBlock->freeNode.nextFree = otherblock->freeNode.nextFree;
        }
        else {
            currentBlock->freeNode.nextFree = NULL;
        }
    }
    else if (Flag == 6){ //Coallescing current block to the one before it 
        if (otherblock->freeNode.nextFree != NULL)
        {
            otherblock -> freeNode.nextFree -> freeNode.prevFree = currentBlock;
            currentBlock -> freeNode.nextFree = otherblock -> freeNode.nextFree;
        }
        else{
            currentBlock -> freeNode.nextFree = NULL;
        }
    }
}


Block* searchList(size_t reqSize) {
    Block* ptrFreeBlock = first_block();
    long int checkSize = -reqSize;


    while (next_block(ptrFreeBlock) != NULL) {
        if (ptrFreeBlock->info.size <= checkSize) {
            return ptrFreeBlock;
        }
        ptrFreeBlock = next_block(ptrFreeBlock);
    }
    if (ptrFreeBlock == first_block() && ptrFreeBlock->info.size <= checkSize) {
        return ptrFreeBlock;
    }
    
    // To begin, you can ignore the free list and just go through every single
    // block in your memory looking for a free space big enough.
    //
    // Return NULL when you cannot find any available node big enough.

    return NULL;
}

/* Find a free block of at least the requested size in the free list.  Returns
   NULL if no free block is large enough. */
Block* searchFreeList(size_t reqSize) {

    Block* ptrFreeBlock = free_list_head;
    long int checkSize = -reqSize;

    while (ptrFreeBlock != NULL) {
    if (ptrFreeBlock->info.size <= checkSize && ptrFreeBlock->info.size < checkSize-16) {
        return ptrFreeBlock;
    }
    ptrFreeBlock = ptrFreeBlock->freeNode.nextFree;
    }

    return NULL;
}

// TOP-LEVEL ALLOCATOR INTERFACE ------------------------------------

/* Allocate a block of size size and return a pointer to it. If size is zero,
 * returns null.
 */
void* mm_malloc(size_t size) {
    Block* ptrFreeBlock = NULL;
    Block* splitBlock = NULL;
    long int reqSize;

    if (size == 0) {
    return NULL;
    }

    reqSize = size;

    // Round up for correct alignment
    reqSize = ALIGNMENT * ((reqSize + ALIGNMENT - 1) / ALIGNMENT);

    if (heap_size == 0) {  //If the heap size is 0, we need more memory
        ptrFreeBlock = requestMoreSpace(reqSize+16);
        Block* currentBlock = (Block*)ptrFreeBlock;
        currentBlock -> info.size = reqSize;
        currentBlock -> info.prev = NULL;
        malloc_list_tail = currentBlock;
    }
    else {
        ptrFreeBlock = searchFreeList(reqSize);
        if (ptrFreeBlock == NULL) {
            ptrFreeBlock = requestMoreSpace(reqSize+16);
            Block* currentBlock = (Block*)ptrFreeBlock;
            currentBlock -> info.size = reqSize;
            currentBlock -> info.prev = malloc_list_tail;
            malloc_list_tail = currentBlock;
        }
        else{
            // Check if the free block is larger than the requested size
            if (ptrFreeBlock->info.size < -reqSize) {
                int emptySpace = ptrFreeBlock->info.size;    // Store the empty space
                Block* currentBlock = (Block*)ptrFreeBlock;     // Cast the pointer to a Block
                currentBlock -> info.size = reqSize;        //Set the size of the block to the requested size

                //Now we need to deal with the space left behind since the block we used was too large
                splitBlock = (Block*)UNSCALED_POINTER_ADD(ptrFreeBlock, reqSize + 16);       // Create a new block
                splitBlock -> info.size = reqSize + emptySpace + sizeof(BlockInfo);           // Set the size of the new block
                splitBlock -> info.prev = (Block*)UNSCALED_POINTER_SUB(splitBlock, reqSize + sizeof(BlockInfo));   // Set the previous block of the new block to currentBlock (the one we just used for requested size)
                Block* temp = UNSCALED_POINTER_ADD(splitBlock, -(splitBlock->info.size - sizeof(BlockInfo))); //Go check to see if splitBlock has a block after it
                if (temp != (Block*) mem_heap_hi) temp->info.prev = splitBlock;      //If the address after SplitBlock is still in the heap, it's previous block is now splitBlock
                if (currentBlock == malloc_list_tail) malloc_list_tail = splitBlock;    //If the block after ptrFreeBlock is null, splitBlock becomes the last block in the heap
                free_List_Manager(currentBlock, splitBlock, 1); //Update the free list
            }
            else {
                ptrFreeBlock->info.size = reqSize;
                free_List_Manager(ptrFreeBlock, NULL, 2); //Update the free list
            }

        }
    }
    return UNSCALED_POINTER_ADD(ptrFreeBlock, sizeof(BlockInfo));
}

void coalesce(Block* blockInfo) {
    Block* nextBlock = next_block(blockInfo);
    Block* previousBlock = blockInfo->info.prev;
    
    if (nextBlock != NULL && previousBlock != NULL && nextBlock->info.size <= 0 && previousBlock->info.size <= 0) //All the current block is surrounded by free blocks
    {
        if(next_block(nextBlock) != NULL){
            next_block(nextBlock) -> info.prev = previousBlock;
        }
        else malloc_list_tail = previousBlock;

        previousBlock -> info.size += ((blockInfo->info.size) - 16) + ((nextBlock->info.size) - 16);
        free_List_Manager(previousBlock, nextBlock, 4); //Update the free list

    }
    else if (nextBlock != NULL && nextBlock ->info.size <= 0)    //If the current block has a free block after it 
    {
        if(next_block(nextBlock) != NULL){
            next_block(nextBlock) -> info.prev = blockInfo;
        }
        else malloc_list_tail = blockInfo;
        
        blockInfo -> info.size += ((nextBlock->info.size) - 16);
        free_List_Manager(blockInfo, nextBlock, 5); //Update the free list
    }
    else if (previousBlock != NULL && previousBlock ->info.size <= 0) //If the current block has a free block before it 
    {

        if (nextBlock == NULL){
            malloc_list_tail = previousBlock;
        }
        else{
            nextBlock -> info.prev = previousBlock;
        }

        previousBlock -> info.size += ((blockInfo->info.size) - 16);
        free_List_Manager(previousBlock, blockInfo, 4); //Update the free list
    }

}

/* Free the block referenced by ptr. */
void mm_free(void* ptr) {
    Block* blockInfo = (Block*)UNSCALED_POINTER_SUB(ptr, sizeof(BlockInfo));
    blockInfo->info.size = -blockInfo->info.size;

    if (next_block(blockInfo) == NULL) 
        malloc_list_tail = blockInfo->info.prev;

    free_List_Manager(blockInfo, NULL, 3); //Update the free list
    coalesce(blockInfo);
}

// PROVIDED FUNCTIONS -----------------------------------------------
//
// You do not need to modify these, but they might be helpful to read
// over.

/* Get more heap space of exact size reqSize. */
void* requestMoreSpace(size_t reqSize) {
  void* ret = UNSCALED_POINTER_ADD(mem_heap_lo(), heap_size);
  heap_size += reqSize;

  void* mem_sbrk_result = mem_sbrk(reqSize);
  if ((size_t)mem_sbrk_result == -1) {
    printf("ERROR: mem_sbrk failed in requestMoreSpace\n");
    exit(0);
  }

  return ret;
}

/* Initialize the allocator. */
int mm_init() {
  free_list_head = NULL;
  malloc_list_tail = NULL;
  heap_size = 0;

  return 0;
}

/* Gets the first block in the heap or returns NULL if there is not one. */
Block* first_block() {
  Block* first = (Block*)mem_heap_lo();
  if (heap_size == 0) {
    return NULL;
  }

  return first;
}

/* Gets the adjacent block or returns NULL if there is not one. */
Block* next_block(Block* block) {
  size_t distance = (block->info.size > 0) ? block->info.size : -block->info.size;

  Block* end = (Block*)UNSCALED_POINTER_ADD(mem_heap_lo(), heap_size);
  Block* next = (Block*)UNSCALED_POINTER_ADD(block, sizeof(BlockInfo) + distance);
  if (next >= end) {
    return NULL;
  }

  return next;
}

/* Print the heap by iterating through it as an implicit free list. */
void examine_heap() {
  /* print to stderr so output isn't buffered and not output if we crash */
  Block* curr = (Block*)mem_heap_lo();
  Block* end = (Block*)UNSCALED_POINTER_ADD(mem_heap_lo(), heap_size);
  fprintf(stderr, "heap size:\t0x%lx\n", heap_size);
  fprintf(stderr, "heap start:\t%p\n", curr);
  fprintf(stderr, "heap end:\t%p\n", end);

  fprintf(stderr, "free_list_head: %p\n", (void*)free_list_head);

  fprintf(stderr, "malloc_list_tail: %p\n", (void*)malloc_list_tail);

  while(curr && curr < end) {
    /* print out common block attributes */
    fprintf(stderr, "%p: %ld\t", (void*)curr, curr->info.size);

    /* and allocated/free specific data */
    if (curr->info.size > 0) {
      fprintf(stderr, "ALLOCATED\tprev: %p\n", (void*)curr->info.prev);
    } else {
      fprintf(stderr, "FREE\tnextFree: %p, prevFree: %p, prev: %p\n", (void*)curr->freeNode.nextFree, (void*)curr->freeNode.prevFree, (void*)curr->info.prev);
    }

    curr = next_block(curr);
  }
  fprintf(stderr, "END OF HEAP\n\n");

  curr = free_list_head;
  fprintf(stderr, "Head ");
  while(curr) {
    fprintf(stderr, "-> %p ", curr);
    curr = curr->freeNode.nextFree;
  }
  fprintf(stderr, "\n");
}

/* Checks the heap data structure for consistency. */
int check_heap() {
  Block* curr = (Block*)mem_heap_lo();
  Block* end = (Block*)UNSCALED_POINTER_ADD(mem_heap_lo(), heap_size);
  Block* last = NULL;
  long int free_count = 0;

  while(curr && curr < end) {
    if (curr->info.prev != last) {
      fprintf(stderr, "check_heap: Error: previous link not correct.\n");
      examine_heap();
    }

    if (curr->info.size <= 0) {
      // Free
      free_count++;
    }

    last = curr;
    curr = next_block(curr);
  }

  curr = free_list_head;
  last = NULL;
  while(curr) {
    if (curr == last) {
      fprintf(stderr, "check_heap: Error: free list is circular.\n");
      examine_heap();
    }
    last = curr;
    curr = curr->freeNode.nextFree;
    if (free_count == 0) {
      fprintf(stderr, "check_heap: Error: free list has more items than expected.\n");
      examine_heap();
    }
    free_count--;
  }

  return 0;
}
