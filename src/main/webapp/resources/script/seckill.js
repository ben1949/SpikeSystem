//存放主要交互逻辑代码
//javascript 模块化
//seckill.detail.init(params);
var seckill = {
    //封装秒杀相关ajax的url
    URL : {
        now :function(){
            return '/seckill/time/now';
        },
        exposer:function(seckillId){
            return '/seckill/' + seckillId + '/exposer';
        },
        execution:function(seckillId,md5){
            return '/seckill/' + seckillId +"/" + md5 + "/execution";
        }

    },
    validataPhone : function(phone){
        if (phone && phone.length==11 && !isNaN(phone)){
            return true;

        } else {
            return false;
        }

    },
    handleSeckillkill :function(seckillId,node) {
        //处理秒杀逻辑
        //获取秒杀地址，控制显示逻辑
        node.hide().html('<button class="btn btn-primary btn-lg" id ="killBtn">开始秒杀</button>');
        $.post(seckill.URL.exposer(seckillId),{},function(result){
            if(result && result['success']) {
                var exposer = result['data'];
                if (exposer['exposed']){
                    //开始秒杀
                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId,md5);
                    console.log("killUrl == "+ killUrl);
                    $('#killBtn').one('click',function(){
                        $(this).addClass('disabled');
                        $.post(killUrl,{},function(result){
                            if (result && result['success'])
                            {

                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                console.log("goodKillResult ==" + killResult);
                                //显示秒杀结果
                                node.html('<span class="label label-success">' + stateInfo+ '</span>')
                            } else {
                                console.log("badKillResult ==" + result);
                            }
                        });
                    });
                    node.show();

                }else {
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    seckill.countdown(seckillId,now,start,end);

                }
            } else {
                console.log("result = "+result);
            }

        });

    },
    countdown:function(seckillId,nowTime,startTime,endTime) {
        var seckillBox = $('#seckill-box');
        console.log("seckill-box == "+seckillBox);
        if(nowTime >endTime) {
            //秒杀结束
            seckillBox.html("秒杀结束");
        } else if (nowTime < startTime) {
            //秒杀还没有开始 计时绑定
            var killTime = new Date(startTime + 1000);
            seckillBox.countdown(killTime,function(event){
                var format = event.strftime('秒杀计时：%D day %H h %M m %S s');
                seckillBox.html(format);
            }).on('finish countdown',function(){
                //获取秒杀地址,
                seckill.handleSeckillkill(seckillId,seckillBox);
            });
        } else {
            //秒杀开始
            seckill.handleSeckillkill(seckillId,seckillBox);

        }
    },
    //详情页秒杀逻辑
    detail:{
        //详情页初始化
        init: function(params){
            //手机验证和登录,计时交换
            //规划我们的交互流程
            //在cookie中查找手机号
            var killPhone = $.cookie('killPhone');

            //console.log("killphone ="+killPhone);
            if(!seckill.validataPhone(killPhone)){
                //绑定手机号
                var killPhoneModal = $('#killPhoneModal');
                killPhoneModal.modal({
                    show:true,
                    backdrop:'static', //禁止位置关闭
                    keyboard:false, //关闭键盘事件
                });
                $('#killPhoneBtn').click(function(){
                    var inputPhone = $('#killPhoneKey').val();
                    console.log("inputPhone ="+inputPhone);
                    if(seckill.validataPhone(inputPhone))
                    {
                        $.cookie('killPhone',inputPhone,{expires:7,path:'/seckill'});
                        //刷新页面，重新走init
                        window.location.reload();
                    } else {
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误</label>').show(300);
                    }
                });
            }
            //计时面板
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            $.get(seckill.URL.now(),{},function(result){
                if(result && result['success']) {
                    var nowTime = result['data'];
                    //计时判断
                    seckill.countdown(seckillId,nowTime,startTime,endTime);
                } else {
                    console.log('result ='+ result);
                }

            });
            //秒杀进行。。。。


        }

    }
};