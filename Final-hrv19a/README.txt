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