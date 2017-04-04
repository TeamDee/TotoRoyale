Git Workflow

git status

git add [relevant files you want to track]

git commit -m [comment]

git pull origin master

(resolve merge conflicts if necessary, goto git status again) 

git push origin master

(alternatively, if you want to start your own branch...) 

git checkout master // Get on your local master branch

git fetch upstream // Fetch the team's repo

git rebase upstream/master // Rebase the team's changes (you want to keep all the current history!)

git branch -l "myBranchName" // Create a new local branch for the changes you want to make

git checkout myBranchName // Get on your newly created branch

Make some changes...

git add . // Add the changes. The . is just a file path, this add's the current directory

git commit -m "I made some changes" // Commit the changes, leave a descriptive message

git push origin myBranchName // Push your changes to your remote fork

Now, go to the team's repo and submit a Pull Request. It'll get merged when we see all is good!
