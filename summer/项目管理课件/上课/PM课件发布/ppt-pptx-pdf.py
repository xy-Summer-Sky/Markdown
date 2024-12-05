#%%
from spire.presentation import *
from spire.presentation.common import *
import os
# 设置文件夹路径和输出文件夹路径
folder_path = os.getcwd()
print(folder_path)
output_folder = os.path.join(folder_path,"Output")
if not os.path.exists(output_folder):
    os.makedirs(output_folder)
if not os.access(output_folder, os.W_OK):
    raise PermissionError(f"Write permission denied for the directory: {output_folder}")
print("Output folder: ", output_folder)

# 遍历文件夹中的文件
for file_name in os.listdir(folder_path):
    file_path = os.path.join(folder_path, file_name)

    # 判断文件名是否以.pptx或.ppt结尾
    if file_name.lower().endswith('.pptx') or file_name.lower().endswith('.ppt'):

        # 根据文件名生成输出路径
        output_path = os.path.join(output_folder, os.path.splitext(file_name)[0] + '.pdf')

        # 创建Presentation对象并从文件加载演示文稿
        presentation = Presentation()
        presentation.LoadFromFile(file_path)

        # 将演示文稿保存为PDF格式到指定输出文件夹
        presentation.SaveToFile(output_path, FileFormat.PDF)

        # 释放Presentation对象占用的资源
        presentation.Dispose()
#%%

#%%
