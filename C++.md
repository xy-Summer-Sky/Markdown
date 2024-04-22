# Chapter file

C++ does not impose structure on a file

## 顺序文件

顺序文件和随机访问文件在实现上的区别主要体现在数据的存储结构和访问方法上。这些区别决定了它们在不同应用场景中的适用性和性能表现。

### 顺序文件的实现

1. **存储结构**：
   - 顺序文件通常将数据按照一定的顺序（例如，按键值升序或降序）存储在文件中。数据记录连续存放，没有间隔。
   - 文件通常从头到尾被读取或写入，数据添加通常发生在文件的末尾。

2. **访问方法**：
   - 读取顺序文件时，通常从文件的开始位置按顺序读取数据，直到找到所需记录或文件结束。
   - 更新或删除操作可能需要重写整个文件或大部分文件，因为修改一个记录的位置可能需要移动其后的所有记录。

3. **优化**：
   - 缓冲技术可以用来提高顺序读写的效率，特别是在处理大型文件时。
   - 文件维护（如压缩文件以消除删除记录后留下的空白）可以定期进行以保持存储效率。

## random access file

固定record长度（字节数）

#### 随机访问文件的实现

1. #### **存储结构**：

   - 随机访问文件不需要按特定顺序存储数据记录。每条记录可以独立存取，通常通过索引或指针直接访问。
   - 文件中的记录可以有固定的长度（固定长度记录更容易直接定位和访问），也可以是变长的（通常需要额外的索引结构来管理）。

2. #### **访问方法**：

   - 可以直接定位到文件中任何位置读取或写入数据，不需要顺序读取整个文件。这通常通过记录的偏移量实现，偏移量可能是基于文件开始的字节位置。
   - 更新和删除操作更为灵活和高效，因为可以直接定位到特定记录进行修改，无需影响其他记录。

3. #### **优化**：

   - 使用索引结构（如B树、哈希表等）可以极大提高随机访问文件的检索效率。
   - 文件碎片管理和优化对于维持随机访问文件的性能至关重要，尤其是在频繁更新和删除的环境中。

### creat random access file

outFile.write(reinterpret_cast<const char *>&number ),sizeof(number ))

![image-20240422150111562](F:\CODE\GIThub\Markdown\图片\image-20240422150111562.png)

reinterpret_cast类型转换，write要求类型是const char*,不同于之前的动态转换，这里的类型转换是在编译阶段已经转换好了

###### Opening a File for Output in Binary Mode

In Fig.14.11,line 11 creates an ofstream object for the file credit.dat.The second argument to the constructor-**ios:out | ios:binary**-indicates that we are opening the file for output in binary mode,which is required if we are to write **fixed-length** records.Multiple file-open modes are combined by separating each open mode from the next with the **|** operator,which is known as the bitwise inclusive OR operator.

![image-20240422152250590](F:\CODE\GIThub\Markdown\图片\image-20240422152250590.png)

Function **seekp** sets the put file-position pointer to a specific
position in the file,then function write outputs the data.

![image-20240422152548886](F:\CODE\GIThub\Markdown\图片\image-20240422152548886.png)

## 实现技术比较

- **顺序文件**更倾向于使用简单的文件系统API，如标准的读/写操作。它们的实现通常更简单，对新手更友好。
- **随机访问文件**可能依赖于更复杂的文件系统操作和数据结构，如内存映射文件（memory-mapped files）或数据库管理系统的底层支持。

## 14.11-p550-Object Serialization(序列化)

Problems

- When object data members are output to a disk file,we lose the object's type information
- only an object's data members were input or output

c++不提供这个方法，但是有第三方库可以实现序列化，解决对象输入输出问题