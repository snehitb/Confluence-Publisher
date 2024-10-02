This backend API service provides endpoints to add a page in Confluence Space as well as edit an existing page



Steps to follow while deployment 

->Clone the repo

->Configure your Confluence tokens while deployment

->From the root of the repo , run the docker file with commands

   -> docker build -t confluence-app .
   
   -> docker run -d -p 8080:8080 --name container125 confluence-app
   
->Once the container is running , Postman can be used to perform the operations

Creating a new page in confluence space

Supply necessary details like page title , page content, space key as well as pageId(optional)

![image](https://github.com/user-attachments/assets/d9753ef9-cad0-44f0-8e56-f1397ba6b966)
![image](https://github.com/user-attachments/assets/74a0bda2-9c0a-43f9-8deb-ec7bdf4689f9) 


link https://snehitbatra.atlassian.net/wiki/spaces/MFS/pages/4882434/testing+from+local



