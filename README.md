# HeadwayTestTask

A Simple Android Mobile Application for searching Github repositories which has been implemented using Clean Architecture alongside MVVM design.


### Technologies & Methodologies which used:

- MVVM Pattern
- Material UI
- RxJava
- LiveData
- Retrofit
- Room
- Firebase Auth
- Paging
- DataBinding

### The App Scenario

Using the [Github API](https://docs.github.com/en/), develop a simple application that fetches the repositories and displays them in a list:
- Searching for results is available only after authorization through a GitHub account.
- Repositories are sorted in desc order by the number of stars.
- Clicking on the list item will open a browser with information about the repository, item will be marked as viewed and insert into the local database.
- User can navigate to a screen with the history of reviewing repositories. The history will contain the last 20 viewed items. It works offline.

### Supported Android Versions

Targets Android Version:
- Android 11.0 ( R ), (API level 30)

Minimum Android Version:
- Android 5.0 (Lollipop), (API level 21)

# App usage
Auth:
![Auth](https://i.imgur.com/3g45UFu.gifB)

Search:
![Search](https://media.giphy.com/media/d2IOau2ltxK5i1Lv1T/giphy.gif?cid=790b7611545ae1d61fe91ef023a85ee4402ecb2b25889edc&rid=giphy.gif&ct=g)

Offline:
![Offline](https://i.imgur.com/eU3x6w5.gif)

# Problems and bugs

According to the [Github doc](https://docs.github.com/en/rest/reference/search#rate-limit), the search API has a standard Rate Limit of 10 queries per minute. After the 10th request, the server will return 403, and further search and pagination will not be available. When i was trying to fix this bug by creating a request which uses OAuth to increase RateLimit to 30 requests per minute, it failed to get access token from Firebase AuthResult (method is described in [documentation](https://firebase.google.com/docs/auth/android/github-auth), but it's missing).
One solution I came up with is to make a custom authorization and generate an authorization token from login+password, but there is no guarantee that more than 30 requests per minute will not be generated.