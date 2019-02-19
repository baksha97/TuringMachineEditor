# TuringMachineEditor
Quick turing machine editor scratched up for personal usages.
![alt text](https://i.gyazo.com/c6f72e4ec494bc475f35a2a28468fe9f.png)

## Features
- Open files
- Save files
- In window editor: ignores empty lines, lines with "//", and trailing/leading whitespace.
- In window tape display
- Labels for previous & next execution
- Current Turing Machine state
- Count for # of executions on machine
- Numerical conversions for current numbers on tape
- Enter "Step by" amount to jump to a particular point in execution
- Use ENTER KEY as an alternative to clicking on the Step button repeatedly (you may hold it down as well)
- Every time you "Run" or "Step" a file in the current working directory is updated with the current program as to not lose any work if you program an infinite loop.
- An output area to view previous tapes and executions.
- States, although commonly represented by numbers are actually strings -so you may use actual words for each Quadruple.



![alt text](https://i.gyazo.com/ca5d25371dab7b4dce3c1dbb2dcacab9.png)

# x^2
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
