package com.sematime.xclone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;


@Route("")
@CssImport("./styles/styles.css")
public class MainView extends VerticalLayout {

     private static Map<String, Post> postMap = new HashMap<>();
@Route("comment")
public class CommentView extends VerticalLayout implements BeforeEnterObserver {

    private String postId;

    public CommentView() {
        setPadding(true);
        setSpacing(true);
        setWidthFull();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        postId = event.getLocation()
                .getQueryParameters()
                .getParameters()
                .getOrDefault("id", java.util.List.of(""))
                .get(0);

        removeAll();

        Post targetPost = MainView.getPostById(postId);

        if (targetPost == null) {
            add(new H3("Post not found."));
            return;
        }

        H3 title = new H3("Replying to @" + targetPost.getUsername());
        Span originalPost = new Span(targetPost.getContent());
        originalPost.getStyle().set("font-style", "italic").set("color", "gray");

        TextArea replyField = new TextArea();
        replyField.setPlaceholder("Write your reply...");
        replyField.setWidthFull();
        replyField.setHeight("150px");

        Button replyButton = new Button("Reply", e -> {
            String replyText = replyField.getValue();
            if (!replyText.trim().isEmpty()) {
                Post reply = new Post("You", replyText);
                targetPost.addReply(reply);
                Notification.show("Reply posted", 3000, Notification.Position.TOP_CENTER);
                UI.getCurrent().navigate(""); // back to main view
            } else {
                Notification.show("Comment cannot be empty", 3000, Notification.Position.MIDDLE);
            }
        });

        add(title, originalPost, replyField, replyButton);
    }

    // Allow MainView to set which post is being replied to
    public static void setReplyTarget(Post post) {
        Post parentPost = post;
    }
}


    private static final boolean isCurrentUser = false;
    private static final Post post = null;
    private boolean showForYou = true;
    private Div contentArea;

    public MainView() {
        setSizeFull();
        setSpacing(false);
        setPadding(false);
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);

        add(createTopBar());
        add(createToggleTabs());

        contentArea = new Div();
        contentArea.setWidthFull();
        contentArea.getStyle().set("margin-bottom", "60px");
        add(contentArea);

        populatePosts();

        add(createFloatingButton());
        add(createBottomBar());
    }
public class Post {
    private String username;
    private String content;
    private List<Post> replies = new ArrayList<>();

    public Post(String username, String content) {
        this.username = username;
        this.content = content;
    }

    public String getUsername() { return username; }
    public String getContent() { return content; }
    public List<Post> getReplies() { return replies; }

    public void addReply(Post reply) {
        replies.add(reply);
    }
}
public static Post getPostById(String id) {
    return postMap.get(id);
}



    private void showPage(String page) {
        contentArea.removeAll();

        switch (page) {
            case "home":
                populatePosts();
                break;
         case "search":
                contentArea.add(new H3("Search"), new Paragraph("Search functionality coming soon..."));
                break;
            case "reload":
                populatePosts();
                Notification.show("Timeline refreshed");
                break;
            case "notifications":
                contentArea.add(new H3("Notifications"));
                for (int i = 1; i <= 5; i++) {
                    contentArea.add(new Paragraph("🔔 You have a new follower: user" + i));
                }
                break;
            case "messages":
                contentArea.add(new H3("Messages"));
                for (int i = 1; i <= 3; i++) {
                    contentArea.add(new Paragraph("📩 Message from user" + i + ": Hey there!"));
                }
                break;
            case "people":
                contentArea.add(new H3("Suggested People to Follow"));
                for (int i = 1; i <= 5; i++) {
                    int index = i;
                    HorizontalLayout suggestion = new HorizontalLayout();
                    Image avatar = new Image("https://i.pravatar.cc/40?img=" + (index + 20), "avatar");
                    avatar.setWidth("40px");
                    avatar.setHeight("40px");
                    avatar.getStyle().set("border-radius", "50%");
                    Span name = new Span("User" + index);
                    Button follow = new Button("Follow", e -> Notification.show("Followed User" + index));
                    suggestion.add(avatar, name, follow);
                    contentArea.add(suggestion);
                }
                break;
        }
    }

    private Component createTopBar() {
        HorizontalLayout bar = new HorizontalLayout();
        bar.setWidthFull();
        bar.getStyle().set("background-color", "#121212")
                .set("padding", "10px")
                .set("align-items", "center")
                .set("justify-content", "space-between")
                .set("border-bottom", "1px solid #222");

        Image avatar = new Image("https://abs.twimg.com/sticky/default_profile_images/default_profile_400x400.png", "Avatar");
        avatar.setWidth("32px");
        avatar.setHeight("32px");
        avatar.getStyle().set("border-radius", "50%");
        avatar.addClickListener(e -> Notification.show("Profile clicked"));

        Icon logo = new Icon(VaadinIcon.TWITTER);
        logo.setColor("#1DA1F2");
        logo.setSize("30px");

        Span title = new Span("X Clone");
        title.getStyle().set("color", "#FFFFFF").set("font-weight", "bold").set("font-size", "20px");

        Icon search = new Icon(VaadinIcon.SEARCH);
        search.setColor("#FFFFFF");
        search.setSize("24px");

        bar.add(logo, title, search);
        bar.setVerticalComponentAlignment(Alignment.CENTER, logo, title, search);
        bar.expand(title);

        return bar;
    }

    private Component createToggleTabs() {
        HorizontalLayout tabs = new HorizontalLayout();
        tabs.setWidthFull();
        tabs.setJustifyContentMode(JustifyContentMode.CENTER);
        tabs.getStyle().set("background-color", "#000").set("color", "#fff").set("padding", "10px 0");

        Button forYou = new Button("For You", e -> {
            showForYou = true;
            populatePosts();
        });
        Button following = new Button("Following", e -> {
            showForYou = false;
            populatePosts();
        });

        tabs.add(forYou, following);
        return tabs;
    }

   private void populatePosts() {
    contentArea.removeAll();

    addPost("Liam Johnson", "liam_dev", "2h ago",
        "Just finished deploying my first microservice on Kubernetes. Huge learning curve but totally worth it!",
        null);

addPost("Sarah Mugo", "sarah.codes", "1h ago",
        "Here’s my workspace setup for 2025! Clean, minimal, and productive. 💻☕",
        "https://images.unsplash.com/photo-1504384308090-c894fdcc538d"); // Desk setup

addPost("Mark Kibet", "marktechie", "3h ago",
        "Attending the Nairobi Tech Week. So many brilliant minds in one room!",
        "https://images.unsplash.com/photo-1551836022-d5d88e9218df"); // Tech conference

addPost("Emily Wangari", "emwangari", "5h ago",
        "Sometimes a walk in nature is all you need to clear your head. 🌳🍃",
        "https://images.unsplash.com/photo-1506744038136-46273834b3fb"); // Nature walk

addPost("Daniel Otieno", "otieno_d", "30m ago",
        "Tried out the new Jetpack Compose APIs today. Android dev just got a lot more fun.",
        "https://images.unsplash.com/photo-1605379399642-870262d3d051"); // Android dev

addPost("Aisha Said", "aishainsights", "6h ago",
        "This new coffee place in town has the best cappuccino and vibes.",
        "https://images.unsplash.com/photo-1541167760496-1628856ab772"); // Coffee shop

addPost("Kevin Kimani", "kimani_kevin", "4h ago",
        "Building a finance tracker app in Vaadin – already loving the flow and structure.",
        null); // No image

addPost("Grace Njeri", "gracenjeri", "2h ago",
        "Read an incredible blog on cybersecurity best practices. Highly recommend it to all devs out there.",
        null); // No image

addPost("Brian Ouma", "ouma.codes", "3h ago",
        "Late night coding sessions are my therapy. 🎧💻",
        "https://images.unsplash.com/photo-1517433456452-f9633a875f6f"); // Night coding

addPost("Natalie Wambui", "nataliewrites", "7h ago",
        "Just wrote a new piece on women in tech in Kenya. Progress is happening, and it's inspiring.",
        null);

}


private void addPost(String name, String handle, String time, String text, String imageUrl) {
    Component post = createPost(name, handle, time, text, imageUrl);
    contentArea.addComponentAtIndex(0, post); // Adds to top of feed
}


    private Component createPost(String name, String handle, String time, String text, String imageUrl) {
        HorizontalLayout post = new HorizontalLayout();
        post.setWidthFull();
        post.setPadding(true);
        post.getStyle()
                .set("border-bottom", "1px solid #222")
                .set("padding", "10px")
                .set("background-color", "#000")
                .set("color", "#fff");

        Image avatar = new Image("https://i.pravatar.cc/40?img=" + (int) (Math.random() * 70 + 1), "avatar");
        avatar.setWidth("40px");
        avatar.setHeight("40px");
        avatar.getStyle().set("border-radius", "50%");

        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setPadding(false);
        contentLayout.setSpacing(false);
        contentLayout.setWidthFull();
        contentLayout.getStyle().set("font-size", "14px");

        HorizontalLayout header = new HorizontalLayout();
        header.setSpacing(true);
        Span nameSpan = new Span(name);
        nameSpan.getStyle().set("font-weight", "bold").set("color", "#fff");
        Span handleSpan = new Span("@" + handle);
        handleSpan.getStyle().set("color", "#aaa");
        Span timeSpan = new Span("· " + time);
        timeSpan.getStyle().set("color", "#aaa");
        header.add(nameSpan, handleSpan, timeSpan);

        Paragraph content = new Paragraph(text);
        content.getStyle().set("color", "#eee");

        contentLayout.add(header, content);


        if (imageUrl != null && !imageUrl.isEmpty()) {
    Image postImage = new Image(imageUrl, "Post image");
    postImage.setWidth("100%");
    postImage.setMaxHeight("400px");
    postImage.getStyle().set("border-radius", "12px");
     contentLayout.add(postImage);
}


        HorizontalLayout actions = new HorizontalLayout();
        actions.getStyle().set("margin-top", "8px").set("gap", "30px").set("color", "#aaa");

        actions.add(
                createCountableButton(VaadinIcon.COMMENT),
                createCountableButton(VaadinIcon.RETWEET),
                createCountableButton(VaadinIcon.HEART),
                createCountableButton(VaadinIcon.SHARE)
        );

        // Show delete button if it's your post
        if (isCurrentUser) {
            Button delete = new Button("Delete", e -> contentArea.remove(post));
            delete.getStyle().set("color", "#f44336").set("background", "transparent").set("border", "none");
            actions.add(delete);
        }

        contentLayout.add(actions);
        post.add(avatar, contentLayout);
        post.setAlignItems(FlexComponent.Alignment.START);
        return post;
    }
private Component createCountableButton(VaadinIcon iconType) {
    Icon icon = new Icon(iconType);
    icon.setColor("#AAA");
    Span counter = new Span("0");
    counter.getStyle().set("margin-left", "5px");

    final boolean[] liked = {false}; // for like button toggle

    Button button;

    if (iconType == VaadinIcon.COMMENT) {
        button = new Button(icon, e -> openCommentDialog()); // ← only change for comment
    } else {
        button = new Button(icon, e -> {
            if (iconType == VaadinIcon.HEART) {
                if (!liked[0]) {
                    int count = Integer.parseInt(counter.getText()) + 1;
                    counter.setText(String.valueOf(count));
                    icon.setColor("#E0245E"); // red heart
                } else {
                    int count = Integer.parseInt(counter.getText()) - 1;
                    counter.setText(String.valueOf(Math.max(0, count)));
                    icon.setColor("#AAA");
                }
                liked[0] = !liked[0];
            } else {
                int count = Integer.parseInt(counter.getText()) + 1;
                counter.setText(String.valueOf(count));
            }
        });
    }
      button.getStyle().set("background", "transparent").set("border", "none").set("color", "#aaa");
    HorizontalLayout layout = new HorizontalLayout(button, counter);
    layout.setAlignItems(FlexComponent.Alignment.CENTER);
    return layout;
    }
private void openCommentDialog() {
    Dialog dialog = new Dialog();
    dialog.setModal(true);
    dialog.setDraggable(false);
    dialog.setResizable(false);
    dialog.setWidth("90%");
    dialog.setMaxWidth("400px");

    VerticalLayout layout = new VerticalLayout();
    layout.setPadding(true);
    layout.setSpacing(true);
    layout.getStyle().set("background-color", "#000").set("color", "#fff");

    Label replyTo = new Label("Replying to @user");
    replyTo.getStyle().set("font-weight", "bold").set("color", "#1DA1F2");

    TextArea commentArea = new TextArea();
    commentArea.setPlaceholder("Post your reply");
    commentArea.setWidthFull();
    commentArea.setHeight("100px");

    Button replyButton = new Button("Reply", click -> {
        String comment = commentArea.getValue();
        if (!comment.trim().isEmpty()) {
            Notification.show("Reply sent: " + comment, 3000, Notification.Position.BOTTOM_CENTER);
            dialog.close();
        } else {
            Notification.show("Reply cannot be empty");
        }
    });
    replyButton.getStyle()
        .set("background-color", "#1DA1F2")
        .set("color", "white")
        .set("border-radius", "20px");

    layout.add(replyTo, commentArea, replyButton);
    dialog.add(layout);
    dialog.open();
}

private Component createToggleButton(VaadinIcon iconType, String activeColor) {
    Icon icon = new Icon(iconType);
    icon.setColor("#AAA");
    Span counter = new Span("0");
    counter.getStyle().set("margin-left", "5px");

    final boolean[] toggled = {false};

    Button button = new Button(icon, e -> {
        int count = Integer.parseInt(counter.getText());
        if (!toggled[0]) {
            count++;
            icon.setColor(activeColor);
        } else {
            count = Math.max(0, count - 1);
            icon.setColor("#AAA");
        }
        counter.setText(String.valueOf(count));
        toggled[0] = !toggled[0];
    });

    button.getStyle()
        .set("background", "transparent")
        .set("border", "none")
        .set("color", "#aaa");

    HorizontalLayout layout = new HorizontalLayout(button, counter);
    layout.setAlignItems(FlexComponent.Alignment.CENTER);
    return layout;
}

private Component createPostComponent(Post post) {
    VerticalLayout postLayout = new VerticalLayout();
    postLayout.setPadding(false);
    postLayout.setSpacing(false);
    postLayout.setWidthFull();
    postLayout.getStyle()
        .set("border-left", "2px solid #e0e0e0")
        .set("padding-left", "10px")
        .set("margin-left", "10px");

    // Main content
    Span username = new Span("@" + post.getUsername());
    username.getStyle().set("font-weight", "bold");

    Span content = new Span(post.getContent());

    HorizontalLayout interactions = new HorizontalLayout(
        createToggleButton(VaadinIcon.HEART, "#E0245E"),
        createCommentButton(post),
        createToggleButton(VaadinIcon.RETWEET, "#17BF63"),
        createToggleButton(VaadinIcon.SHARE, "#657786")
    );

    postLayout.add(username, content, interactions);

    // Recursively add replies
    for (Post reply : post.getReplies()) {
        postLayout.add(createPostComponent(reply));
    }

    return postLayout;
}

private Button createCommentButton(Post post) {
    Button commentBtn = new Button(VaadinIcon.COMMENT.create());
    commentBtn.addClickListener(e ->
        UI.getCurrent().navigate("comment?post=" + post.hashCode())
    );
    return commentBtn;
}

    private Component createBottomBar() {
        HorizontalLayout bar = new HorizontalLayout();
        bar.setWidthFull();
        bar.getStyle()
                .set("background-color", "#000000")
                .set("position", "fixed")
                .set("bottom", "0")
                .set("left", "0")
                .set("right", "0")
                .set("padding", "10px 0")
                .set("justify-content", "space-around")
                .set("z-index", "1000")
                .set("border-top", "1px solid #222");

        bar.add(
                createNavButton(VaadinIcon.HOME, "Home", "home"),
                createNavButton(VaadinIcon.SEARCH, "Search", "search"),
                createNavButton(VaadinIcon.REFRESH, "Reload", "reload"),
                createNavButton(VaadinIcon.BELL, "Notifications", "notifications"),
                createNavButton(VaadinIcon.ENVELOPE, "Messages", "messages"),
                createNavButton(VaadinIcon.USERS, "People", "people")
        );

        return bar;
    }

    private Button createNavButton(VaadinIcon iconType, String tooltip, String pageName) {
        Icon icon = new Icon(iconType);
        icon.setColor("#FFFFFF");
        icon.setSize("26px");

        Button button = new Button(icon);
        button.getStyle().set("background", "transparent").set("border", "none");
        button.getElement().setProperty("title", tooltip);
        button.addClickListener(e -> showPage(pageName));

        return button;
    }

    private Component createFloatingButton() {
        Div fab = new Div();
        fab.getStyle()
                .set("position", "fixed")
                .set("bottom", "80px")
                .set("right", "20px")
                .set("background", "#1DA1F2")
                .set("color", "#fff")
                .set("width", "56px")
                .set("height", "56px")
                .set("border-radius", "50%")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("cursor", "pointer")
                .set("box-shadow", "0 4px 10px rgba(0,0,0,0.4)")
                .set("z-index", "1500");

        Icon pen = new Icon(VaadinIcon.PENCIL);
        pen.setColor("white");
        pen.setSize("28px");
        fab.add(pen);

        fab.addClickListener(e -> showComposeDialog());

        return fab;
    }

    private void showComposeDialog() {
        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);

        VerticalLayout layout = new VerticalLayout();
        layout.setWidth("300px");

        TextArea postText = new TextArea("What's happening?");
        postText.setWidthFull();
        postText.setHeight("100px");

        Button postButton = new Button("Post", e -> {
            contentArea.addComponentAtIndex(0, createPost(
                    "You",
                    "you",
                    "now",
                    postText.getValue(),
                    "https://picsum.photos/seed/temp/400/200"
            ));
            dialog.close();
        });

        layout.add(postText, postButton);
        dialog.add(layout);
        dialog.open();
    }
}
