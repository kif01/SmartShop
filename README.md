# Smart-Shop
## Description
An IBM Cloud-powered mobile app that assists you in your shopping. The app recognizes the products that you want to purchase in a store by taking pictures of their IDs. Instead of waiting at the counter to scan every item and pay there, the app can automate the entire process. Must note that the integration with a real payment system is not part of this project, this is a PoC that focuses more on recognizing the products using IBM Cloud services. Check the below for GIF a better understanding. Must note that I don't have actual physical products so instead I was taking pictures of images on the screen as if they are actual real products in a shop.

![ezgif com-resize (2)](https://user-images.githubusercontent.com/15332386/88187913-33faaf80-cc48-11ea-9e19-bf4c04812720.gif)

## Technologies
- Android
- Openshift
- MAX OCR Model (Text Extraction AI Model)
- Cloudant DB
- Watson Studio

## Architecture
<img width="754" alt="Screen Shot 2020-07-19 at 11 23 58 AM" src="https://user-images.githubusercontent.com/15332386/87869682-abafac80-c9b2-11ea-8c5e-b039d0711c84.png">

1- We feed Cloudant DB with a JSON sample data of products. <br>
2- User opens the camera on his mobile app and takes a picture of the product's ID.<br>
3- The image is sent via a POST request to MAX OCR Model deployed on Openshift. The model processes the image to extract the text (ID of the product) and sends it back to Android.<br>
4- Android accesses the product's document in Cloudant using the product's ID extracted from the previous step (The ID of the products are stored in Cloudant to access them correspondingly), and gets the information of the product to display it to the user. When the user clicks on pay in the app, the total quantity of the corresponding product gets updated and incremented to keep track of the number of units being sold.<br>
5- Watson Studio is connected to Cloudant DB where we have the data. We visualize this data to get a better understanding of our products (Like checking the number of units of every product that have been sold, or the number of sales for every product).<br>

## Screenshots

<img width="598" alt="Screen Shot 2020-07-19 at 4 06 55 AM" src="https://user-images.githubusercontent.com/15332386/87864120-5e611a00-c975-11ea-87f1-6e94c82dcec4.png">
