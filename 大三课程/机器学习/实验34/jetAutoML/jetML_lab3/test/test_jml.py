from pzmllog import NewLogger

data_length = 10
epoch = 3
learning_rate = 0.001

log = NewLogger(
    config={
   
        'port': "5560",
        'access_token':"ih2dbbfw9ul3p5sk8k9us16m",
'project':"1530",

'description':"test",

'experiment_name':"TEST",

 'repository_id':"3a19f3e283eb4089ae6e681fa78dcc72"
    },
    #超参数集
    info = {
        "learning_rate": learning_rate,
        "epoch": epoch,
        "batch_size": 64
    }
)


for e in range(epoch):
    # 开始实验
    log.Run()
    for i in range(data_length):
        # 实验部分
        loss = 0.1*(10 - i)
        accuracy = (0.1 * i)
        # 记录 Log
        log.Log({"epoch":epoch,"loss":loss,"accuracy":accuracy})
    # 实验模型路径
    model_path = "model.pt"
    # 记录模型
    log.Save([model_path])
    # 结束实验
    log.End()
# 结束整个过程
log.Submit()