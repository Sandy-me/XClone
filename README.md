# XClone - Twitter Clone Using Vaadin

**XClone** is a lightweight Twitter clone web app built with the [Vaadin Flow Framework](https://vaadin.com/). It mimics core Twitter features and demonstrates modern UI/UX concepts in Java with minimal backend setup.


## Features

-  Create text and image posts
-  Like posts with toggle interaction
-  Comment on posts via a dedicated comment page
-  Retweet and Share functionality
-  Threaded/Nested comment replies
-  Responsive layout

## Tech Stack

- **Java 17+**
- **Vaadin 24+**
- **Maven**


##  Project Structure

xclone/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── sematime/
│   │   │           └── xclone/
│   │   │               ├── MainView.java
│   │   │               ├── CommentView.java
│   │   │               └── components/
│   │   │                   ├── PostComponent.java
│   │   │       
│   │   └── resources/
│   │       └── application.properties
├── pom.xml
└── README.md

## Getting Started

### Prerequisites

* Java 17+
* Maven
* Internet connection to download Vaadin dependencies

### Run the app

mvn clean install
mvn compile jetty:run


Visit: [http://localhost:8080](http://localhost:8080)


## How Commenting Works

Clicking the 💬 **Comment button** on any post navigates to a `/comment?id={postId}` page where the user can:

* See the post ID
* Type a comment in a text area
* Press `Reply` to post a comment (to be wired up to storage or displayed inline)

## Notes

* The current app is a **UI proof-of-concept**, not yet connected to a database.
* State is temporarily managed in-memory via static maps or placeholder components.


## License

This project is licensed under the MIT License.

## Acknowledgments

* [Vaadin Documentation](https://vaadin.com/docs)
* UI/UX inspiration from Twitter

## Author

[https](https://github.com/Sandy-me)
