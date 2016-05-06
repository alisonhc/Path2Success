# Path2Success

This is a simple app that allows the user to input goals. The user can input the goal title,
along with a due date and a category that can be used to filter goals.
Additionally, the user can edit an existing goal and even add a new category.
When a goal is checked off, it will then appear on the goal history page. The user can go to this page and uncheck a goal,
which will then appear back on the home screen.

    ## How the app is organized:

    Home screen with list of all goals: can filter display via navigation drawer using "All" or any goal category.
        - From navigation drawer, the user can click on the History page
        - Long click on a goal on the home scrren to go to Edit page with title, date, and category options
        - Click red floating button to go to to Input page with title, date, and category options
        - Long click on a goal on the history page to delet the goal permanently

    ## Install and run this app
        1. Copy github url.
        2. Open Android Studio; go to VCS -> checkout from version control -> GitHub
        3. Enter password if it asks you; then paste url into Git Repository URL space
        4. Make sure you have it saved in the correct Parent Directory, then click Clone
        5. Is this new project open? Find HomeScreenActivity (app -> src -> main -> java -> com.example.tyler.Path2Success)
        6. Plug in android phone; make sure it is on debug mode
        7. Click run.
        8. After gradle builds (might take awhile), the app should launch and give you a simple tutorial.
        7. Have fun!

Disclosure:
Open Source https://github.com/amlcurran/ShowcaseView for the app tutorial when launched for the first time.
