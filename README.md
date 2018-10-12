# Next Lesson Widget
![Next Lesson Widget Logo]([https://lh3.googleusercontent.com/9fIROE-ZQVYbLpAa12E1jWh_v11MeQN8gmwb9QI2UT2vDZI9M8joZwZ_zPivHH3rew=s180)

Simple android widget that tell the next lesson the user will follow based on a csv file containing his school program.

Now on the play store: [Download here!](https://play.google.com/store/apps/details?id=com.lucblender.lucasbonvin.widgettest )

## Csv file format

- every line must describe a lesson
- all lessons -> chronological order Monday to Sunday
- the format of a line is: 
    - lessonName,Room,hh:mm start,hh:mm end,Location
- lesson eg: 
    - "Wed,Sin,A205,8:45,11:10,Sion
        
## User interface

### Widget user interface:
- Widget, next lesson
### In app user interface:
- Main user interface:
    - Preferences
    - Choosing csv file
    - Opening this csv file with any app
    - Help and About
- Secondary user interface:
    - Full planner
    - Planner editable:
    - Long click to delete a lesson
    - Plus button to add a lesson
