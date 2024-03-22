# News Service

This service provides a RESTful API for managing news articles and their associated comments.

## Endpoints

### POST /news
Creates a new news article. The request body should contain a JSON representation of the `NewsDto` object. Returns the created `NewsDto` object.

### GET /news
Returns a paginated list of all news articles. You can specify the page number and page size as query parameters. You can also specify the page size for the comments of each news article.

### GET /news/search
Searches for news articles based on the provided text and fields. Returns a paginated list of matching news articles. You can specify the page number and page size as query parameters. You can also specify the page size for the comments of each news article.

### GET /news/{id}
Returns the news article with the specified ID. You can specify the page size for the comments of the news article as a query parameter.

### GET /news/{id}/comments
Returns a paginated list of all comments for the news article with the specified ID. You can specify the page number and page size as query parameters.

### GET /news/{newsId}/comments/{commentId}
Returns the comment with the specified ID from the news article with the specified ID.

### PUT /news
Updates an existing news article. The request body should contain a JSON representation of the `NewsDto` object. Returns the updated `NewsDto` object.

### DELETE /news/{id}
Deletes the news article with the specified ID.

## Data Transfer Objects

### NewsDto
Represents a news article. Contains the following fields:
- `id`: The ID of the news article.
- `time`: The time the news article was posted.
- `title`: The title of the news article.
- `text`: The text of the news article.
- `comments`: A list of comments on the news article.

### CommentDto
Represents a comment on a news article. Contains the following fields:
- `id`: The ID of the comment.
- `time`: The time the comment was posted.
- `text`: The text of the comment.
- `userName`: The name of the user who posted the comment.
- `newsId`: The ID of the news article the comment is associated with.

### DOC
JSON format:<br/>
http://localhost:8082/v1/v3/api-docs<br/>
YAML format:<br/>
http://localhost:8082/v1/v3/api-docs.yaml<br/>
SWAGGER:<br/>
http://localhost:8082/v1/swagger-ui/index.html<br/>