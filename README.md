
# TuringMachineEditor
This program allows creation, loading, editing, and stepping through your own Turing Machine programs with the three commands:
1. Move left
2. Move right
3. Print

## Instructions / How-to's

### Input Tape

 - In this machine, you may initialize your starting tape 1 of 2 ways.
	 1. Entering the tape itself: ``B111B111``
		 - If you enter the tape itself, it must contain a `B`to be entered as a manual tape entry. Otherwise, it will be considered a numeric entry. 
		 - 
			 **Acceptable input:**
			 - [x] `B111`
			 - [x] `111B`
			 - [x] `BBBBB111`
			 - [x] `B 1 1  1` 
			 - [x] `B,1,1,1,B`
	2. To easily enter an input tape, simply add the number. Multiple inputs require a `,` as a separator. 
  *Avoid excess white-space and numbers that would cause integer overflow.*
		- **Acceptable input:**
			 - [x] ``2``
			 - [x]  ``28``
			 - [x] `7,6`
			 - [x] `3,4,9,7` 

### Program Editor
 - Editor ignores/works around:
	 - Empty lines (space out your work)
	 - Lines containing "//" (comment or take out a line instead of deleting it)
	 - Trailing/Leading white-space (so you don't have to worry about checking for white-space)
 - The quadruple states are treated as Strings; you do not need to use numbers for the states & you may append letters to numbers to make a valid state, for example; 
    ```
    10d,B,L,1m
    1m,1,L,1c 
    ```
 - Every time you click "Step" or "Run," the current program in the editor is saved automatically to a text-file `current-program.txt` in your current working directory. This is so that if you accidentally program yourself into an infinite loop and the machine freezes, you have your work saved.
 - The editor automatically loads `current-program.txt` if it is located in the current working directory.
 - You may add new Quadruples into the editor without having to reset your machine, just click Run or Step and it will add your Quadruple to the machine and unhalt it if a state is found for it's current state. **BE WARY OF EDITING YOUR PROGRAM THIS WAY.** You may change your program so-much-so that you the machine will be unable to get back to that position if you've removed or edited a Quadruple that got you to your current state. If you're careful, this can be very helpful in programming something from scratch and adding new lines step by step.

### Execution
 - Once you've set your program by clicking "Setup/Reset" or using `SHIFT-ENTER`, you may now run your program or step through it.
	 - Every time you step through your program, you will leave a history in the output area to go back and revise previous steps and executions.
	 - You can choose the amount of steps you would like to take per "Step" button click or `ENTER` hits. 
	 - *HINT: If you seem to have an error in your code, narrow your clicking and your searching by using the number of steps to jump to a specific execution.*
- If you would like to use `ENTER` to execute your program, do so in any of the text fields (not the program editor). You may also hold down the `ENTER` and watch as the ListView moves across each cell.
- As the tape expands, the list grows horizontally; you may scroll to have a better view of the tape or you may simply widen your program window. Once a cell has been expanded to, it does not ever shrink in space.
- Instead of counting all the ones, you may look at the numbers in the square brackets. After every step and execution, the numbers are recalculated and shown there. 
- Be wary of bigger inputs. They work. *Mostly*. 

### File Management

 - The File Manager automatically opens on the current working directory.
 - You may open any `*.txt`file into the editor. 
	 - When you open a file, remember that you're not changing the file. You've opened it into the current editor's memory. Again, once you click Step/Run, the `current-program.txt` is then overwritten with the current editor contents. 
- You may save anything in your editor to a separate text file. 

### TO - DO's

 - [ ] Find a way to prevent the user from deselecting the selected (positioned) cell while still giving them the ability to freely scroll the ListView.
 - [ ] Denote the position of the machine also by bolding the cell. Plan to: -change cell text to "|x|" -if cell.contains("|") *bold*..
 - [ ] Fix execution count overlap on MacOS
 
#### File Open/Save 
![Stepping](https://i.gyazo.com/3a7b9edcb0fe8a18fede4753345ad45c.gif)

#### Step 
![Stepping](https://i.gyazo.com/dea7f2e620604dbfb75f794192782093.gif)

#### Run
![Running](https://i.gyazo.com/0d4be619f0d233bd4709db7c02559f9b.gif)

#### Program: x^2
```
1,B,R,299
299,1,B,399
399,B,R,499
499,1,R,499
499,B,R,599
599,1,R,599
599,B,1,699
699,1,R,699
699,B,L,799
799,1,L,799
799,B,L,899
899,1,L,899
899,B,L,999
999,B,1,1099
1099,1,R,1099
1099,B,R,1199
1199,1,1,299
1199,B,L,1299
1299,B,1,1399
1399,1,L,1399
1399,B,R,1499
1499,1,B,199
199,B,L,2

1m,B,L,2
2,B,1,3
3,1,R,3
3,B,R,4
4,B,R,4
4,1,R,5
5,1,R,5
5,B,R,6
6,B,R,6
6,1,R,7
7,1,R,7
7,B,R,8
8,1,R,8
8,B,1,9
9,1,L,9
9,B,L,10
10,1,L,10
10,B,L,19
19,1,L,11
19,B,L,19
11,1,L,11
11,B,R,12
12,1,B,13
13,B,R,14
14,1,L,15
15,B,L,15
15,1,L,16
16,1,L,16
16,B,B,2
14,B,R,17
17,1,B,18
17,B,R,27
27,B,R,27
18,B,R,29
27,1,1,17
29,B,L,22
29,1,L,20
20,B,L,20
20,1,L,21
21,1,L,21
21,B,B,1m
22,B,L,22
22,1,B,23
23,B,L,24
24,1,1,22
24,B,R,25
25,B,R,25
25,1,L,26
```
