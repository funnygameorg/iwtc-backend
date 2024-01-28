#!/bin/bash

# S3 초기화

echo 'start [create bucket]'

# 원본 이미지 버킷 생성
awslocal s3api create-bucket --bucket media
# 1/2 썸네일 버킷 생성
awslocal s3api create-bucket --bucket media-divide2

# 생성 버킷 리스트 확인
awslocal s3api list-buckets

echo 'end [create bucket]'





# ----------------------------------------

# 임시 미디어 파일 오브젝트 생성

# 이미지가 로컬스택에 저장되는 위치
localstack_image_path=/etc/localstack/init/ready.d

# 예시 이미지 : 1/2 썸네일
thumbnail_value=$(cat $localstack_image_path/thumbnail-divide2-image.txt)
echo -n $thumbnail_value > thumbnail.txt

# 예시 이미지 : 원본
original_value=$(cat $localstack_image_path/original-image.txt)
echo -n $original_value > origin.txt

echo -n "https://www.youtube.com/watch?v=M1jBQzM8wdU" > vedioId1.txt
echo -n "https://www.youtube.com/watch?v=Y2FjO0P0H6Q" > vedioId2.txt
echo -n "https://www.youtube.com/watch?v=cC8EolQTzbE" > vedioId3.txt
echo -n "https://www.youtube.com/watch?v=IHjOBM2P7vU" > vedioId4.txt
echo -n "https://www.youtube.com/watch?v=vl9lwn9jQwY" > vedioId5.txt






# ----------------------------------------
# 버킷에 오브젝트 삽입

# 원본 오브젝트 삽입
awslocal s3 cp origin.txt s3://media/a
awslocal s3 cp origin.txt s3://media/b
awslocal s3 cp origin.txt s3://media/c
awslocal s3 cp origin.txt s3://media/d
awslocal s3 cp origin.txt s3://media/e
awslocal s3 cp origin.txt s3://media/f
awslocal s3 cp origin.txt s3://media/g
awslocal s3 cp origin.txt s3://media/h
awslocal s3 cp origin.txt s3://media/i

# 썸네일 오브젝트 삽입
awslocal s3 cp thumbnail.txt s3://media-divide2/a
awslocal s3 cp thumbnail.txt s3://media-divide2/b

# 유튜브 URL 오브젝트 삽입
awslocal s3 cp vedioId1.txt s3://media/j
awslocal s3 cp vedioId2.txt s3://media/k
awslocal s3 cp vedioId3.txt s3://media/l
awslocal s3 cp vedioId4.txt s3://media/m
awslocal s3 cp vedioId5.txt s3://media/n

