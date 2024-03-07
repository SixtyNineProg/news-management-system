# Comment Service

This service provides a RESTful API for managing comments.

## Endpoints

### POST /comments
Creates a new comment. The request body should contain a JSON representation of the `CommentDto` object. Returns the created `CommentDto` object.

### GET /comments
Returns a paginated list of all comments. You can specify the page number and page size as query parameters.

### POST /comments/filter
Returns a paginated list of all comments that match the provided filter. You can specify the page number and page size as query parameters. The request body should contain a JSON representation of the `CommentsFilter` object.

### POST /comments/delete
Deletes all comments that match the provided filter. The request body should contain a JSON representation of the `CommentsFilter` object.

### GET /comments/search
Searches for comments based on the provided text and fields. Returns a paginated list of matching comments. You can specify the page number and page size as query parameters.

### GET /comments/{id}
Returns the comment with the specified ID.

### PUT /comments
Updates an existing comment. The request body should contain a JSON representation of the `CommentDto` object. Returns the updated `CommentDto` object.

### DELETE /comments/{id}
Deletes the comment with the specified ID.

## Data Transfer Objects

### CommentDto
Represents a comment. Contains the following fields:
- `id`: The ID of the comment.
- `time`: The time the comment was posted.
- `text`: The text of the comment.
- `userName`: The name of the user who posted the comment.
- `newsId`: The ID of the news article the comment is associated with.


### DOC
JSON format:<br/>
http://localhost:8081/v1/v3/api-docs<br/>
YAML format:<br/>
http://localhost:8081/v1/v3/api-docs.yaml<br/>
SWAGGER:<br/>
http://localhost:8081/v1/swagger-ui/index.html<br/>