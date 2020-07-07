(* 

Author : Mohammad Khan
Date: 5/30/20
Pledge: I pledge my honor that I have abided by the Stevens Honor System.

*)

type program = int list

(* Problem-1 *)

let rec colored': int -> int -> (int*int) list -> program -> (int*int) list =
  fun curr_x curr_y coords program ->
  match program with
  | [] -> coords
  | h1::h2::t when h1=1 -> 
      if h2<>0 then (curr_x,curr_y)::coords (* stops coloring when pen is up *)
      else colored' (curr_x) (curr_y) ((curr_x,curr_y)::coords) t
  | h::t when h=1 -> colored' (curr_x) (curr_y) ((curr_x,curr_y)::coords) t 
  | h::t when h=2 -> colored' (curr_x) (curr_y+1) ((curr_x,curr_y)::coords) t 
  | h::t when h=3 -> colored' (curr_x+1) (curr_y) ((curr_x,curr_y)::coords) t 
  | h::t when h=4 -> colored' (curr_x) (curr_y-1) ((curr_x,curr_y)::coords) t 
  | h::t when h=5 -> colored' (curr_x-1) (curr_y) ((curr_x,curr_y)::coords) t 
  | _ -> failwith "invalid command"

  let rec mem e l =
    match l with
    | [] -> false
    | h::t -> h=e || mem e t
                
  let rec remove_duplicates l =
    match l with
    | [] -> []
    | h::t when mem h t -> remove_duplicates t
    | h::t -> h::remove_duplicates t

  
let rec colored : (int*int) -> program -> (int*int) list = 
  fun start program -> 
   match program with
   | [] -> []
   | h::t when h=0 -> remove_duplicates @@ colored' (fst start) (snd start) [] t 
   | _ -> failwith "invalid starting command"



(* Problem-2 *)

(* both coords1 and coords2 are same length and all elements are unique *)
let rec equivalent': (int*int) list -> (int*int) list -> bool =
  fun coords1 coords2 ->
   match coords1 with 
   | [] -> true
   | h::t -> mem h coords2 && equivalent' t coords2 

let rec equivalent: program -> program -> bool =
  fun program1 program2 ->
    let coords1 = colored (0,0) program1 in 
    let coords2 = colored (0,0) program2 in 
    if (List.length coords1)<>(List.length coords1) then false
    else if coords1=[] then true 
    else equivalent' coords1 coords2 
  


(* problem3 *)

let mirror (instruction: int):int  = 
  match instruction with 
  | 0 -> 0
  | 1 -> 1
  | 2 -> 4
  | 3 -> 5
  | 4 -> 2
  | 5 -> 3
  | _ -> failwith "invalid command"

let mirror_image : program -> program =
  fun program -> List.map mirror program 


(* problem 4  *)

let rotate (instruction: int):int  = 
  match instruction with 
  | 0 -> 0
  | 1 -> 1
  | 2 -> 3
  | 3 -> 4
  | 4 -> 5
  | 5 -> 2
  | _ -> failwith "invalid command"

let rotate_90 : program -> program =
  fun program -> List.map rotate program 


(* problem 5 *)

let rec repeat: int -> 'a -> 'a list =
  fun n x ->
   match n with 
   | 0 -> []
   | m when m>0 -> x::repeat (n-1) x
   | _ -> failwith "invalid n"
 

(* problem 6 *)

(* a *)
let rec pantograph : int -> program -> program =
  fun factor program ->
   match program with 
   | [] -> []
   | h::t when h=0 || h=1 -> h::pantograph factor t 
   | h::t when h>1 && h<=5 -> (repeat factor h) @ pantograph factor t 
   | _ -> failwith "invalid command"

  (* b *)
  let pantogram_m' : int -> int -> int list =
    fun factor command  ->
    match command with 
     | n when n=0 || n=1 -> [n]
     | n when n>1 && n<=5 -> repeat factor command 
     | _ -> failwith "invalid command"

  
  let pantograph_m : int -> program -> program =
    fun factor program -> List.concat @@ List.map (pantogram_m' factor) program
  
  (* c *)

  let pantogram_f' : int -> int list-> int -> int list =
    fun factor res command ->
      match command with 
      | n when n=0 || n=1 -> res @ [n]
      | n when n>1 && n<=5 -> res @ repeat factor command 
      | _ -> failwith "invalid command"
      
     

  let pantograph_f : int -> program -> program =
    fun factor program -> List.fold_left (pantogram_f' factor) [] program

  (* problem 7 *)

  let rec compress' i l =
    match l with 
    | [] -> []
    | [x] -> [(x,i)]
    | h1::h2::t ->
      if h1=h2 then compress' (i+1) (h2::t)
      else (h1,i) :: compress' 1 (h2::t) 

  let compress : program -> (int*int) list =
    fun l -> compress' 1 l 

  
  (* problem 8 *)

  (* a *)
  let rec uncompress' : (int*int) list -> program list =
    fun l -> 
     match l with 
      | [] -> []
      | h::t ->  [pantogram_m' (snd h) (fst h) ] @ uncompress' t 


  let uncompress : (int*int) list -> program =
    fun l -> List.flatten @@ uncompress' l

  (* b *)
  let uncompress_m' : (int*int) -> program =
    fun tup  ->
    match (fst tup) with 
     | n when n=0 || n=1 -> [n]
     | n when n>1 && n<=5 -> repeat (snd tup) (fst tup) 
     | _ -> failwith "invalid command"

  let uncompress_m :  (int*int) list -> program =
    fun l -> List.flatten @@ List.map uncompress_m' l 

  (* c *)

  let uncompress_f' : int list-> (int*int) -> int list =
    fun res tup ->
      match (fst tup) with 
      | n when n=0 || n=1 -> res @ [n]
      | n when n>1 && n<=5 -> res @  ( repeat (snd tup) (fst tup) )
      | _ -> failwith "invalid command"
      

  let uncompress_f : (int*int) list -> program =
    fun l -> List.fold_left uncompress_f' [] l 

    
(* test *)

(* let square : program = [0; 2; 2; 3; 3; 4; 4; 5; 5; 1]
let letter_e : program = [0;2;2;3;3;5;5;4;3;5;4;3;3;5;5;1]
let square2 : program = [0; 3; 3; 2; 2; 5; 5; 4; 4; 1]
let rogue : program = [0;5;5;3;2;4;4;1]
let dual : program = [0; 2; 2; 3; 3; 4; 4; 5; 5; 1; 0;5;5;3;2;4;4;1 ]
let wrong : program = [0; 2; 2; 3; 3; 4; 4; 5; 5; 1;5;5;3;2;4;4;1 ]

let square_colored = colored (0,0) square 
let e_colored = colored (0,0) letter_e 
let square_colored2 = colored (-1,5) square 
let dual_colored = colored (0,0) dual
let wrong_colored = colored (0,0) wrong
let rogue_colored = colored (0,0) rogue 

let equ1 = equivalent square square2
let equ2 = equivalent square letter_e 

let reflect = mirror_image letter_e 

let rotate = rotate_90 letter_e

let copy = repeat 3 " hello " 

let enlarge = pantograph 2 letter_e 
let enlarge2 = pantograph_m 2 letter_e
let enlarge3 = pantograph_f 2 letter_e

let concise = compress letter_e 

let expnad = uncompress ( compress letter_e )
let expnad2 = uncompress_m ( compress letter_e )
let expnad3 = uncompress_f ( compress letter_e ) *)












