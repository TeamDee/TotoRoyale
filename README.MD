
FUNCTIONALTIES OF TIGER ISLAND

TEAM D – we have implemented every requirement asked of us. 

	
	Placement phase is implemented	
			Tile Placement is implemented 
			
	Build phase is implemented 
			Building a totoro sanctuary is implemented (works locally,  but for server reasons we commented it out. (The
			    AI strategy is geared toward building Tiger playgrounds and placing meeples)
			
			Building a tiger playground is implemented 
		
			Creating a new settlement is implemented 
		
	Scoring is implemented 	

	AI is implemented

To get more knowledge on how to run the program, please see TigerIsland_CodeDocumentationandCompilation.docx.













Git Workflow

<b>1. git status </b>

<b>2. git add [relevant files you want to track]</b>

<b>3. git commit -m [comment]</b>

<b>4. git pull origin master</b>

<b>5. (resolve merge conflicts if necessary, goto git status again) </b>

<b>6. git push origin master</b>



(alternatively, if you want to start your own branch...) 

<b>git checkout master</b> // Get on your local master branch

<b>git fetch upstream</b> // Fetch the team's repo

<b>git rebase upstream/master</b> // Rebase the team's changes (you want to keep all the current history!)

<b>git branch -l "myBranchName"</b> // Create a new local branch for the changes you want to make

<b>git checkout myBranchName </b>// Get on your newly created branch

<b>Make some changes...</b>

<b>git add .</b> // Add the changes. The . is just a file path, this add's the current directory

<b>git commit -m "I made some changes" </b>// Commit the changes, leave a descriptive message

<b>git push origin myBranchName </b>// Push your changes to your remote fork

<b>Now, go to the team's repo and submit a Pull Request. It'll get merged when we see all is good!</b>
