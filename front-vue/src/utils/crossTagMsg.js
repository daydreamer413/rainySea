//标签页通信
const  channel = new BroadcastChannel('demo')

export function sendMsg(type,conent){
    channel.postMessage({
        type,
        conent,
    });
}
export function listenMsg(callback){
    const handler = (e) =>{
        callback && callback(e.data)
    }
    channel.addEventListener('message',handler);
    return () =>{
        channel.removeEventListener('message',handler);
    }
}