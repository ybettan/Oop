# Oop

Please follow this rules when working on this directory:

    1. if you are working in windows i recommend to download "git bash terminal"
    2. once you are in a terminal follow numbers [3-end]
    
    3. first register your personal info (need to do only once for all directories)
        - git config --global user.name "Alon Kwart"
        - git config --global user.email "alon.kwart@gmail.com"
    4.  use this to configure default editor for git commit (only once)
        - git config --global core.editor "$(which vim)"
        - you can use a diffrent editor if you wish
    

When you want to add a feature/update one please follow this workflow:

    1. git pull (in master branch)
    2. create branch for the new feature/changes 
    3. checkout branch 
    4. make changes
    5. commit changes (to branch)

 ------after the changes works------  
    
    6. git checkout master 
    7. git pull (to receive last updated - on master)
    8. git checkout branch 
    9. git rebase master (now branch is up to date) 

------after changes works on the most updated project------ 

    10. git checkout master  
    11. git merge branch  
    12. git pull (just to be sure we are up to date)     
        - if there were chages: re-test and go back to 12 

------after all changes are implied on latest update and works------ 

    13. git push 


Important notes:

    * when you commit use the command "commit -s"
        - it will insert your name to the commit

    * after "commit -s" you will be sent to vim, please use this format for commit-message:

        <file path from git repo> : <one line description - only the main idea>
        
            <the detailed message>


    * commit message for example:


        Hw1/file1.jav : added write-permission to MainClass

            for reason bla bla we need to give to MainClass some write 
            premitions so it can bla bla
    
            

