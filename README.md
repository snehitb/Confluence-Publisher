# Confluence Publisher

This application enables users to create and edit existing Confluence pages.

---


## API Endpoints

| HTTP Method | Endpoint               | Description                                   |
|-------------|------------------------|-----------------------------------------------|
| POST        | `/publishCustom`       | Publishes a new page in Confluence using the provided `ConfluenceModel`. |
| POST        | `/editExisting`        | Edits an existing page in Confluence using the provided `ConfluenceModel`. |


The payload ConfluenceModel will contain the following

| Field      | Data Type                      | Description                                             |
|------------|--------------------------------|---------------------------------------------------------|
| `title`    | `String`                       | The title of the Confluence page.                       |
| `content`  | `ArrayList<ArrayList<String>>` | The content of the page, represented as a list of lists of strings. Each inner list can represent a paragraph or section. |
| `pageId`   | `String`                       | The unique identifier of the page to be edited (optional). |
| `spaceKey` | `String`                       | The key of the Confluence space where the page will be created or edited. |




---

## Accessing the Confluence Publisher

### Sign-Up

Supply necessary details like page title , page content, space key as well as pageId(optional)

![image](https://github.com/user-attachments/assets/d9753ef9-cad0-44f0-8e56-f1397ba6b966)
![image](https://github.com/user-attachments/assets/74a0bda2-9c0a-43f9-8deb-ec7bdf4689f9) 


The page created : https://snehitbatra.atlassian.net/wiki/spaces/MFS/pages/4882434/testing+from+local


---

## Technologies Used
- **Spring Boot**
- **Docker**

---

## Running the Application

For easier deployment make sure to have docker installed. 
A docker image has been provided.

To run this application, follow the steps below:
1. Clone the repository.
2. Run these commands

 ### Build the Docker image

 docker build -t confluence-app .

 ### Run the Docker container
 
docker run -d -p 8080:8080 --name container_name confluence-app



