# Quicksort Algorithm Implementation with Buffer Pool

## Project Description

This repository contains my implementation of a modified Quicksort algorithm, a key assignment for CS3114/5040. The project's core challenge was sorting a binary file containing 4-byte records using Quicksort, with an added complexity of managing file I/O through a buffer pool implementing the LRU (Least Recently Used) scheme.

### Key Features

- **Modified Quicksort Algorithm**: Adapted to sort 4-byte records based on a 2-byte key.
- **Buffer Pool Management**: Implementation of a buffer pool to manage file access, utilizing LRU for efficiency.
- **Block-size Oriented I/O**: All I/O operations are performed in blocks of 4096 bytes for optimization.

## Learning Outcomes

- **Algorithm Modification**: Gained experience in modifying classic algorithms (Quicksort) to suit specific data structures and constraints.
- **Efficiency in File Handling**: Learned to efficiently manage file I/O in a memory-constrained environment using buffer pools.
- **Performance Optimization**: Developed an understanding of optimizing algorithms for better performance, particularly in data sorting and file management.

## Usage

Run the program via the command line:
'java Quicksort <data-file-name> <numb-buffers> <stat-file-name>'

- `<data-file-name>`: The binary file to be sorted.
- `<numb-buffers>`: Number of buffers for the buffer pool (1–20).
- `<stat-file-name>`: File to store runtime statistics.

## Output

The program outputs runtime statistics including cache hits, disk reads/writes, and total runtime of the algorithm.
