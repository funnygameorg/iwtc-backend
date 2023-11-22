#!/bin/bash

echo 'start [create bucket]'

# 버킷 생성
awslocal s3api create-bucket --bucket media

# 생성 버킷 리스트 확인
awslocal s3api list-buckets

echo 'end [create bucket]'
