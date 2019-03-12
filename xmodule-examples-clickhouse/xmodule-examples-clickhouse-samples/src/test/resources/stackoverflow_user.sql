CREATE TABLE stackoverflow_user
(
    id UInt64,
    name String,
    reputation UInt32,
    websiteUrl String, 
    location String,
    aboutMe String,
    views UInt32,
    upVotes UInt32,
    downVotes UInt32,
    profileImageUrl String,
    lastAccessTime DateTime,
    createTime DateTime, 
    createDate Date DEFAULT toDate(createTime)
)
ENGINE = MergeTree()
PARTITION BY toYYYYMMDD(createDate)
ORDER BY (id, name, reputation, location, views, upVotes, downVotes, lastAccessTime, createTime);