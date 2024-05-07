Installation:
The user must have maven, java, and Junit installed. All files needed aside from that are included in main/java.

CLI Instructions for Utilization:
The easiest way to compile and use the programs included is to first navigate to the root directory of the project in a terminal, then use the following commands.
For compressing with LZW: java -cp target/classes SchubsL <filename>
Note: filename can be replaced with a glob. This will create a compressed file with the same name and the extension .txt.ll

For compressing with Huffman: java -cp target/classes SchubsH <filename>
Note: filename can be replaced with a glob. This will create a compressed file with the same name and the extension .txt.hh

For archiving files with tar: java -cp target/classes SchubsArc <archive-name> <GLOB> [<GLOB> or filenames...]
Note: this will create the archive at the <archive-name> location in the directory with the .zh extension.

For decompressing and unarchiving: java -cp target/classes Deschubs <filename>
Note: this command is dynamic and will decompress or unarchive according to the extension on <filename> whether it is .ll .hh or .zh.

For running maven tests: mvn test

Design Considerations:
Huffman:
Huffman coding is a widely used algorithm for lossless data compression. Its design revolves around constructing a variable-length prefix coding scheme based on the frequency of occurrence of each symbol in the input data. By assigning shorter codes to more frequent symbols and longer codes to less frequent symbols, Huffman achieves efficient compression. However, it may not always achieve the optimal compression ratio because it operates on individual symbols rather than sequences of symbols, which can lead to suboptimal results, especially for data with uneven symbol frequencies.

LZW:
LZW is another popular compression algorithm, particularly effective for compressing text files. Its design involves building a dictionary of variable-length codes that represent recurring sequences of symbols in the input data. By replacing these sequences with shorter codes, LZW compresses the file. One of the strengths of LZW is its adaptability to the input data, as it dynamically updates the dictionary during compression, meaning the algorithm does not need to include a trie at the front of the file like Huffman does. However, this adaptability also introduces complexity, as both the encoder and decoder need to actively build the symbol table as the algorithm is ongoing. Additionally, LZW's efficiency can be influenced by the size and structure of the input data, with some variations of the algorithm performing better on certain types of data than others.

Tars:
Tars is a method of archiving data. Tars is effective as it is easily understood and decoded. Tars is capable of efficiently holding multiple files archived into one single file. However, Tars on its own does not compress the files, resulting in slightly larger files as it needs to include the file contents and details about the file structure.
