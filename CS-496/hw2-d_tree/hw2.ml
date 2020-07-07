(* 

Author : Mohammad Khan
Date: 6/06/20
Pledge: I pledge my honor that I have abided by the Stevens Honor System.

*)


(* =========================== Part One ============================================ *)

type dTree = 
  | Node of char*dTree*dTree 
  | Leaf of int

let tLeft = Node('w',
              Node('x',
                  Leaf(2),
                  Leaf(5)
              ), 
              Leaf(8)
          )

let tRight = Node('w',
              Node('x',
                  Leaf(2),
                  Leaf(5)
              ), 
              Node('y',
                  Leaf(7),
                  Leaf(5)
              )
          )

(* height defined as number of edges to deepest node *)
let rec dTree_height : dTree -> int = 
  fun t ->
    match t with
      | Leaf(x) -> 0
      | Node(x, l, r) -> 1 + max (dTree_height l) (dTree_height r)

(* size defined as total number of nodes and leaves *)
let rec dTree_size : dTree -> int = 
  fun t ->
    match t with 
    | Leaf(x) -> 1
    | Node(x, l, r) -> 1 + dTree_size l + dTree_size r


(* dTree_paths helper *)
let paths' : int -> int list -> int list = 
  fun v lst -> v :: lst 

(* lists all the paths to leaves | 0=left, 1=right *)
let rec dTree_paths : dTree -> int list list =
  fun t ->
    match t with
    | Leaf(x) -> [[]]
    | Node(x,l,r) -> 
        let left = (dTree_paths l) in 
        let right = (dTree_paths r) in
        ( List.map (paths' 0) left ) @ ( List.map (paths' 1) right )


(* dTree_is_perfect helper *)
let rec perfect' : int list list -> bool =
  fun l ->
    match l with 
    | [] -> true 
    | h1::h2::t -> (List.length h1 = List.length h2) && perfect' (h2 :: t)
    | _ -> true

(* determines if all leaves have same depth *)
let  dTree_is_perfect : dTree -> bool =
  fun t ->
    let paths = (dTree_paths t) in
      if (List.length paths = 1) then true 
      else perfect' paths


(* maps the nodes and leaves of the tree *)
let rec dTree_map : (char->char) -> (int->int) -> dTree -> dTree  =
  fun f g t -> 
    match t with
    | Leaf(x) -> Leaf(g x)
    | Node(x, l, r) -> Node(f x, dTree_map f g l, dTree_map f g r)


(*========================Part Two==================================================== *)

(* creates a tree in which the symbols of an inner node at level n
    corresponds to the n-th element in l. *)
let rec list_to_tree : char list -> dTree =
  fun l ->
    match l with 
    | [] -> Leaf(0)
    | h::t -> Node(h, list_to_tree t, list_to_tree t)

(* finds the value of a specific path in the graph *)
let rec find_res : int list -> (int list * int) list -> int =
  fun lst f ->
    match f with 
    | [] -> failwith "Path not found in the function"
    | h::t -> 
        match h with
          | (path, res) -> 
              if path = lst then res
              else find_res lst t
          
(* replace_leaf_at helper *)
let rec replace : dTree -> (int list * int) list -> int list -> dTree =
  fun t f lst ->
  match t with
  | Leaf(x) -> Leaf( find_res lst f )
  | Node(x,l,r) ->
      Node( x, replace l f (lst @ [0]) , replace r f  (lst @ [1])  )
  
(* replaces all the leaves in t by the value indicated in the graph of the function *)
let replace_leaf_at : dTree -> (int list * int) list -> dTree =
  fun t f ->
    replace t f [] 

(* takes a pair-encoding of a boolean function and
    returns its tree-encoding. *)
let bf_to_dTree : char list * (int list * int) list -> dTree = 
  fun bf ->
    match bf with 
    | ( char_lst, f ) -> replace_leaf_at (list_to_tree char_lst ) f 
 
(* ================================================================================== *)

(* test *)

(* let singleton = Leaf(9)

let t1_1 = dTree_height tLeft
let t1_2 = dTree_height tRight 
let t1_3 = dTree_height singleton 

let t2_1 = dTree_size tLeft
let t2_2 = dTree_size tRight 
let t2_3 = dTree_size singleton 

let t3_1 = dTree_paths tLeft
let t3_2 = dTree_paths tRight
let t3_3 = dTree_paths singleton 

let t4_1 = dTree_is_perfect tLeft
let t4_2 = dTree_is_perfect tRight
let t4_3 = dTree_is_perfect singleton

let t5_1 = dTree_map Char.uppercase_ascii (fun x -> x+1) tLeft
let t5_2 = dTree_map Char.uppercase_ascii (fun x -> x+1) tRight
let t5_3 = dTree_map Char.uppercase_ascii (fun x -> x+1) singleton


let t6 = list_to_tree ['x';'y';'z'] 

let f = [([0;0;0] , 0); 
          ([0;0;1] , 1); 
          ([0;1;0] , 1); 
          ([0;1;1] , 0); 
          ([1;0;0] , 1); 
          ([1;0;1] , 0);
          ([1;1;0] , 0); 
          ([1;1;1] , 1)]

(* let t5_1 = find_res [1;0;0] f 

let t5_2 = replace t4 f []  *)

let t7 = replace_leaf_at t6 f 

let bf = (['x';'y';'z'],
          [([0;0;0], 0);
          ([0;0;1] , 1);
          ([0;1;0] , 1);
          ([0;1;1] , 0);
          ([1;0;0] , 1);
          ([1;0;1] , 0);
          ([1;1;0] , 0);
          ([1;1;1] , 1)])

let t8 = bf_to_dTree bf 
 *)

