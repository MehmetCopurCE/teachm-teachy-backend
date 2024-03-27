# Teachm-Teachy

Teachm-Teachy, ... (uygulamanızın kısa bir açıklaması)

## API Endpoitleri

### Authentication

| Name         | URL                  | Method | Description                                  |
|--------------|----------------------|--------|----------------------------------------------|
| **Register** | `/api/auth/register` | POST   | Creates a new user.                          |
| **Login**    | `/api/users/login`   | POST   | Gets a list of friend requests for the user. |

### Users

| Name                  | URL               | Method | Description                        |
|-----------------------|-------------------|--------|------------------------------------|
| **Create User**       | `/api/users`      | POST   | Creates a new user.                |
| **List Users**        | `/api/users`      | GET    | Lists all users.                   |
| **Get User By Id**    | `/api/users/{id}` | GET    | Gets a user with a specific ID.    |
| **Update User By Id** | `/api/users/{id}` | PUT    | Updates a user with a specific ID. |
| **Delete User By Id** | `/api/users/{id}` | DELETE | Deletes a user with a specific ID. |


### Friendship

| Name                      | URL                                         | Method | Description                                  |
|---------------------------|---------------------------------------------|--------|----------------------------------------------|
| **Send Friend Request**   | `/api/users/{userId}/send-friend-request`   | POST   | Sends a friend request to another user.      |
| **Get Friend Requests**   | `/api/users/{userId}/friend-requests`       | GET    | Gets a list of friend requests for the user. |
| **Accept Friend Request** | `/api/users/{userId}/accept-friend-request` | POST	  | Accepts a friend request from another user.  |
| **Reject Friend Request** | `/api/users/{userId}/reject-friend-request` | POST   | Rejects a friend request from another user.  |
| **Get Friends**           | `/api/users/{userId}/friends`               | GET    | Gets a list of the user's friends.           |


### Posts

| Name                    | URL                          | Method | Description                                                 |
|-------------------------|------------------------------|--------|-------------------------------------------------------------|
| **Get All Posts**       | `/api/posts`                 | GET    | Gets all posts.                                             |
| **Get Posts By UserId** | `/api/posts?userId={userId}` | GET    | Optionally, filter by user ID using userId query parameter. |
| **Create Post**         | `/api/posts`                 | POST   | Creates a new post.                                         |
| **Get Post By Id**      | `/api/posts/{id}`            | GET    | Gets a post by its ID.                                      |
| **Update Post By Id**   | `/api/posts/{id}`            | PUT    | Updates a post with a specific ID.                          |
| **Delete Post By Id**   | `/api/posts/{id}`            | DELETE | Deletes a post with a specific ID.                          |



### Comments

| Name                                     | URL                                             | Method | Description                                                                         |
|------------------------------------------|-------------------------------------------------|--------|-------------------------------------------------------------------------------------|
| **Create Comment**                       | `/api/comments`                                 | POST   | Creates a new comment.                                                              |
| **Get All Comments**                     | `/api/comments`                                 | GET    | Gets all Comments.                                                                  |
| **Get Comments By UserId**               | `/api/comments?userId={userId}`                 | GET    | Optionally, filter by user ID using userId query parameter.                         |
| **Get Comments By PostId**               | `/api/comments?postId={postId}`                 | GET    | Optionally, filter by post ID using postId query parameter.                         |
| **Get Comments By PostId And By UserId** | `/api/comments?userId={userId}&postId={postId}` | GET    | Optionally, filter by user ID and post ID using userId and postId query parameters. |
| **Get Comment By Id**                    | `/api/comments/{id}`                            | GET    | Gets a comment by its ID.                                                           |
| **Update Comment By Id**                 | `/api/comments/{id}`                            | PUT    | Updates a comment with a specific ID.                                               |
| **Delete Comment By Id**                 | `/api/comments/{id}`                            | DELETE | Deletes a comment with a specific ID.                                               |




<br>

## Kurulum

... (uygulamanızı kurmak için gereken adımlar)

## Kullanım

... (uygulamanızı kullanmak için gereken adımlar)

## Katkıda Bulunma

... (uygulamanıza nasıl katkıda bulunabileceklerine dair bilgiler)

## Lisans

... (uygulamanızın lisans bilgisi)
