# Smart-Shop
## Description
An IBM Cloud-powered mobile app that assists you in your shopping. The app recognizes the products that you want to purchase in a store by taking pictures of their IDs. Instead of waiting at the counter to scan every item and pay there, the app can automate the entire process. Must note that the integration with a real payment system is not part of this project, this is a PoC that focuses more on recognizing the products using IBM Cloud services.

## Technologies
- Android
- Openshift
- MAX OCR Model (Text Extraction AI Model)
- Cloudant DB
- Watson Studio

## Architecture
<img width="818" alt="Screen Shot 2020-07-19 at 2 51 21 AM" src="https://user-images.githubusercontent.com/15332386/87863847-18a25280-c971-11ea-9db4-d26bcbd9e567.png">

1- We feed Cloudant DB with a JSON sample data of products. <br>
2- User opens the camera on his mobile app and takes a picture of the product's ID.<br>
3- The image is sent via a POST request to MAX OCR Model deployed on Openshift. The model processes the image to extract the text (ID of the product) and sends it back to Android.<br>
4- Android accesses the product's document in Cloudant using the product's ID extracted from the previous step (The ID of the products are stored in Cloudant to access them correspondingly), and gets the information of the product to display it to the user. When the user clicks on pay in the app, the total quantity of the corresponding product gets updated and incremented to keep track of the number of units being sold.<br>
5- Watson Studio is connected to Cloudant DB where we have the data. We visualize this data to get a better understanding of our products (Like checking the number of units of every product that have been sold, or the number of sales for every product).<br>
