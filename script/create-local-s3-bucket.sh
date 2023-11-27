#!/bin/bash

echo 'start [create bucket]'

# 버킷 생성
awslocal s3api create-bucket --bucket media

# 생성 버킷 리스트 확인
awslocal s3api list-buckets

echo 'end [create bucket]'

# 테스트용 임시 오브젝트 삽입

echo -n "https://picsum.photos/seed/gf/600/800" > url.txt

awslocal s3 cp url.txt s3://media/a
awslocal s3 cp url.txt s3://media/b
awslocal s3 cp url.txt s3://media/c
awslocal s3 cp url.txt s3://media/d
awslocal s3 cp url.txt s3://media/e
awslocal s3 cp url.txt s3://media/f
awslocal s3 cp url.txt s3://media/g
awslocal s3 cp url.txt s3://media/i
awslocal s3 cp url.txt s3://media/j
awslocal s3 cp url.txt s3://media/k
awslocal s3 cp url.txt s3://media/l
awslocal s3 cp url.txt s3://media/m

