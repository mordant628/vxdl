#!/usr/bin/env bash

set -e

function kill_procs () {
    for p in $PING_PID $ES_ONE $ES_TWO $VERTX_PID; do
        test -n "$p" && ( echo "killing pid: $p" ; kill -9 "$p" 2>/dev/null || true)
    done
}


function send_xhr_pings() {
    curl 'http://localhost:9080/eventbus/524/az4xaphu/xhr_send' -H 'Origin: https://localhost' -H 'Accept-Encoding: gzip, deflate, br' -H 'Accept-Language: en-US,en;q=0.8,es-MX;q=0.6,es;q=0.4,fr;q=0.2' -H 'User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.100 Safari/537.36' -H 'Content-Type: text/plain;charset=UTF-8' -H 'Accept: */*' -H 'Referer: https://localhost/eventbus/iframe.html' -H 'Cookie: vertx-web.session=e825bf26-7771-411b-b4e3-78306e9ff746; JSESSIONID=dummy;' -H 'Connection: keep-alive' --data-binary '["{\"type\":\"ping\"}"]' --compressed
    curl 'http://localhost:9080/eventbus/524/bz4xaphu/xhr_send' -H 'Origin: https://localhost' -H 'Accept-Encoding: gzip, deflate, br' -H 'Accept-Language: en-US,en;q=0.8,es-MX;q=0.6,es;q=0.4,fr;q=0.2' -H 'User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.100 Safari/537.36' -H 'Content-Type: text/plain;charset=UTF-8' -H 'Accept: */*' -H 'Referer: https://localhost/eventbus/iframe.html' -H 'Cookie: vertx-web.session=e825bf26-7771-411b-b4e3-78306e9ff746; JSESSIONID=dummy;' -H 'Connection: keep-alive' --data-binary '["{\"type\":\"ping\"}"]' --compressed
}
trap kill_procs EXIT

test -f target/vxdl.jar || ( echo "building project" ; mvn clean install || exit 1)

echo "starting vertx server"
java -jar target/vxdl.jar &

VERTX_PID=$!
echo "server running on PID=$VERTX_PID"
sleep 8

sleep 5
echo "Begin listening to event stream..."
## start listening on the event stream
curl 'http://localhost:9080/eventbus/524/az4xaphu/eventsource' -H 'Accept-Encoding: gzip, deflate, sdch, br' -H 'Accept-Language: en-US,en;q=0.8,es-MX;q=0.6,es;q=0.4,fr;q=0.2' -H 'User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.100 Safari/537.36' -H 'Accept: text/event-stream' -H 'Referer: https://localhost/eventbus/iframe.html' -H 'Cookie: vertx-web.session=e825bf26-7771-411b-b4e3-78306e9ff746; JSESSIONID=dummy;' -H 'Connection: keep-alive' -H 'Cache-Control: no-cache' --compressed &
ES_ONE=$!
curl 'http://localhost:9080/eventbus/524/bz4xaphu/eventsource' -H 'Accept-Encoding: gzip, deflate, sdch, br' -H 'Accept-Language: en-US,en;q=0.8,es-MX;q=0.6,es;q=0.4,fr;q=0.2' -H 'User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.100 Safari/537.36' -H 'Accept: text/event-stream' -H 'Referer: https://localhost/eventbus/iframe.html' -H 'Cookie: vertx-web.session=e825bf26-7771-411b-b4e3-78306e9ff746; JSESSIONID=dummy;' -H 'Connection: keep-alive' -H 'Cache-Control: no-cache' --compressed &
ES_TWO=$!
echo "Event Stream Pids: " $ES_ONE $ES_TWO

sleep 2
echo "sending pings for great justice..."

send_xhr_pings

## register both streams for an address:
echo "Registering both streams to listen on address: foobar123"
curl 'http://localhost:9080/eventbus/524/az4xaphu/xhr_send' -H 'Origin: https://localhost' -H 'Accept-Encoding: gzip, deflate, br' -H 'Accept-Language: en-US,en;q=0.8,es-MX;q=0.6,es;q=0.4,fr;q=0.2' -H 'User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.100 Safari/537.36' -H 'Content-Type: text/plain;charset=UTF-8' -H 'Accept: */*' -H 'Referer: https://localhost/eventbus/iframe.html' -H 'Cookie: vertx-web.session=e825bf26-7771-411b-b4e3-78306e9ff746; JSESSIONID=dummy' -H 'Connection: keep-alive' --data-binary '["{\"type\":\"register\", \"address\":\"foobar123\"}"]' --compressed
curl 'http://localhost:9080/eventbus/524/bz4xaphu/xhr_send' -H 'Origin: https://localhost' -H 'Accept-Encoding: gzip, deflate, br' -H 'Accept-Language: en-US,en;q=0.8,es-MX;q=0.6,es;q=0.4,fr;q=0.2' -H 'User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.100 Safari/537.36' -H 'Content-Type: text/plain;charset=UTF-8' -H 'Accept: */*' -H 'Referer: https://localhost/eventbus/iframe.html' -H 'Cookie: vertx-web.session=e825bf26-7771-411b-b4e3-78306e9ff746; JSESSIONID=dummy' -H 'Connection: keep-alive' --data-binary '["{\"type\":\"register\", \"address\":\"foobar123\"}"]' --compressed

echo "sending pings for great justice..."
send_xhr_pings

## Saying hello:  Both streams should receive this
echo `date` "Saying hello:  Both streams should receive this 'howdy' message after ~7 seconds"
curl 'http://localhost:9080/eventbus/522/bz4xaphu/xhr_send' -H 'Origin: https://localhost' -H 'Accept-Encoding: gzip, deflate, br' -H 'Accept-Language: en-US,en;q=0.8,es-MX;q=0.6,es;q=0.4,fr;q=0.2' -H 'User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.100 Safari/537.36' -H 'Content-Type: text/plain;charset=UTF-8' -H 'Accept: */*' -H 'Referer: https://localhost/eventbus/iframe.html' -H 'Cookie: vertx-web.session=e825bf26-7771-411b-b4e3-78306e9ff746; JSESSIONID=dummy' -H 'Connection: keep-alive' --data-binary '["{\"type\":\"publish\", \"address\":\"foobar123\", \"body\":\"howdy\"}"]' --compressed

sleep 4

echo "sending pings for great justice..."
send_xhr_pings

sleep 4

send_xhr_pings

## and now we induce the deadlock
echo `date` "Inducing Deadlock.  We will send the 'deadlock' message, which has a ~7 second delay for propagation, and we will kill the listening event streams during this delay: "
time curl 'http://localhost:9080/eventbus/522/bz4xaphu/xhr_send' -H 'Origin: https://localhost' -H 'Accept-Encoding: gzip, deflate, br' -H 'Accept-Language: en-US,en;q=0.8,es-MX;q=0.6,es;q=0.4,fr;q=0.2' -H 'User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.100 Safari/537.36' -H 'Content-Type: text/plain;charset=UTF-8' -H 'Accept: */*' -H 'Referer: https://localhost/eventbus/iframe.html' -H 'Cookie: vertx-web.session=e825bf26-7771-411b-b4e3-78306e9ff746; JSESSIONID=dummy' -H 'Connection: keep-alive' --data-binary '["{\"type\":\"publish\", \"address\":\"foobar123\", \"body\":\"deadlock villainy!\"}"]' --compressed

echo `date` "pausing for deadlock..."
sleep 1

echo `date` "killing eventsource streams one..."
kill -TERM $ES_ONE
echo `date` "killing eventsource streams two..."
kill -TERM $ES_TWO

echo -n `date` "waiting excessive amount of time for message propagation..."
sleep 4
echo "...wait for it..."
sleep 4

kill -QUIT $VERTX_PID

sleep 2

echo "Ta-dah!  The evil deadlock black hand causes much wailing and gnashing of teeth"
exit 0
