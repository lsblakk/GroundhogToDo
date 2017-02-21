# Pre-work - Groundhog ToDo

Groundhog ToDo is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item.

Submitted by: Lukas Blakk

Time spent: 30 hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can **successfully add and remove items** from the todo list
* [x] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* [x] User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* [x] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [x] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [ ] Add support for completion due dates for todo items (and display within listview item)
* [x] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing items
* [ ] Add support for selecting the priority of each todo item (and display in listview item)
* [ ] Tweak the style improving the UI / UX, play with colors, images or backgrounds

The following **additional** features are implemented:

* [x] Text validation to prevent empty items
* [x] Add item in a fragment instead of Main Activity
* [x] Custom icon
* [x] Using Toolbar with "+" icon to add new items

## Video Walkthrough 

Here's a walkthrough of implemented user stories for the basic app now with editing in DialogFragment:

<img src='https://github.com/lsblakk/GroundhogToDo/blob/master/groundhog_final_screencap.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

Worked on adding datepicker for some time but have been stuck on getting and setting values from the datepicker so am submitting without that aspect completed.  I did as many of the suggested items as I could in the time I had.  It was fun to learn about Toolbar and I hope to get more time to learn how to layout & customize the UI of an app in future projects.

## License

    Copyright 2017 Lukas Blakk

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
