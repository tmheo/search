# Sample Search Application

## Swagger URL

### http://localhost:8080/swagger-ui.html

## REST API Documentation

### http://localhost:8080/docs/api-guide.html

## Elasticsearch REST Query

### Create Mapping
```
PUT /article
{
  "mappings": {
    "blog": {
      "properties": {
        "title": {
          "type": "text",
          "fields": {
            "keyword": {
              "type": "keyword",
              "ignore_above": 256
            },
            "suggest": {
              "type": "completion"
            }
          }
        },
        "author": {
          "type": "text",
          "fields": {
            "keyword": {
              "type": "keyword",
              "ignore_above": 256
            },
            "suggest": {
              "type": "completion"
            }
          }
        },
        "tags": {
          "type": "text",
          "fields": {
            "keyword": {
              "type": "keyword",
              "ignore_above": 256
            },
            "suggest": {
              "type": "completion"
            }
          }
        }
      }
    }
  }
}
```

## Get Index Info
```
GET /article?pretty
```

## Get Index Mapping
```
GET /article/_mapping?pretty
```

## Generate sample data
```
POST /article/blog
{
  "title": "apache spark development",
  "author": "Jane Doe",
  "category": "tech",
  "contents" : "apache spark is cool. general big data processing engine",
  "tags": ["apache spark", "big data"],
  "createdAt": "2017-06-16T09:00:11+0900"
}
```

```
POST /article/blog
{
  "title": "elasticsearch study",
  "author": "author1",
  "category": "tech",
  "contents" : "elasticsearch is nosql search engine",
  "tags": ["nosql", "search", "big data"],
  "createdAt": "2017-06-17T19:10:24+0900"
}
```

```
POST /article/blog
{
  "title": "aws lambda",
  "author": "aws guy",
  "category": "tech",
  "contents" : "aws lambda is serverless function",
  "tags": ["aws lambda", "serverless", "cloud"],
  "createdAt": "2017-06-18T08:53:31+0900"
}
```

```
POST /article/blog
{
  "title": "blockchain",
  "author": "John Doe",
  "category": "finance",
  "contents" : "bitcoin will make money?",
  "tags": ["bitcoin", "finance"],
  "createdAt": "2017-06-18T09:25:53+0900"
}
```

## Search by prefix
```
GET /article/blog/_search?pretty
{
    "query" : {
        "match_phrase_prefix" : { "tags" : "big" }
    }
}
```

## Search by prefix for multi field
```
GET /article/blog/_search?pretty
{
  "query": {
    "multi_match": {
      "query": "big",
      "type": "phrase_prefix",
      "fields": [
        "title",
        "author",
        "tags"
      ]
    }
  }
}
```

## Search by query string
```
GET /article/blog/_search?pretty
{
  "query": {
    "query_string": {
      "query": "*doe*",
      "fields": [
        "title",
        "author",
        "tags"
      ]
    }
  }
}
```

## Multi Search with Highlight
```
GET /_msearch?pretty
{"index":"article","type":"blog"}
{"query":{"query_string":{"fields":["title"],"query":"*doe*"}},"highlight":{"pre_tags":[""],"post_tags":[""],"fields":{"title":{}}}}
{"index":"article","type":"blog"}
{"query":{"query_string":{"fields":["author"],"query":"*doe*"}},"highlight":{"pre_tags":[""],"post_tags":[""],"fields":{"author":{}}}}
{"index":"article","type":"blog"}
{"query":{"query_string":{"fields":["tags"],"query":"*doe*"}},"highlight":{"pre_tags":[""],"post_tags":[""],"fields":{"tags":{}}}}
```

## Completion Suggest
```
GET /article/blog/_search?pretty
{
  "query": {
    "match_all": {}
  },
  "size": 0,
  "suggest": {
    "title.suggest": {
      "prefix": "ap",
      "completion": {
        "field": "title.suggest"
      }
    },
    "author.suggest": {
      "prefix": "ap",
      "completion": {
        "field": "author.suggest"
      }
    },
    "tags.suggest": {
      "prefix": "ap",
      "completion": {
        "field": "tags.suggest"
      }
    }
  }
}
```
