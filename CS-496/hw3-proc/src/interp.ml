
(* 

Author : Mohammad Khan
Date: 6/14/20
Pledge: I pledge my honor that I have abided by the Stevens Honor System.

*)

open Ast
open Ds
            
let (>>*) c f  =
  fun env ->
  match c env with
  | Ok v -> f v 

let hd' = 
  fun l ->
      match l with 
      | [] ->   error "list is empty"
      | h::_ -> return @@  h 

let tl' = 
  fun l ->
      match l with 
      | [] ->   error "list is empty"
      | _ :: t -> return @@ ListVal t

let empty' = 
  fun l ->
      match l with
      | [] -> return @@ BoolVal true
      | _  -> return @@ BoolVal false


let bt'  = function 
  | NumVal n ->  NumVal n
  |  BoolVal b ->  BoolVal b
  | ListVal l -> ListVal l
  | TreeVal t -> TreeVal t

let b3t = function 
  | Ok res -> res



let rec apply_proc : exp_val -> exp_val -> exp_val ea_result =
  fun f a ->
  match f with
  | ProcVal (id,body,env) ->
    return env >>+
    extend_env id a >>+
    eval_expr body
  | _ -> error "apply_proc: Not a procVal"
and
 eval_expr : expr -> exp_val ea_result = fun e ->
  match e with
  | Int(n) ->
    return @@ NumVal n
  | Var(id) ->
    apply_env id
  | Add(e1,e2) ->
    eval_expr e1 >>=
    int_of_numVal >>= fun n1 ->
    eval_expr e2 >>=
    int_of_numVal >>= fun n2 ->
    return @@ NumVal (n1+n2)
  | Sub(e1,e2) ->
    eval_expr e1 >>=
    int_of_numVal >>= fun n1 ->
    eval_expr e2 >>=
    int_of_numVal >>= fun n2 ->
    return @@ NumVal (n1-n2)
  | Mul(e1,e2) ->
    eval_expr e1 >>=
    int_of_numVal >>= fun n1 ->
    eval_expr e2 >>=
    int_of_numVal >>= fun n2 ->
    return @@ NumVal (n1*n2)
  | Div(e1,e2) ->
    eval_expr e1 >>=
    int_of_numVal >>= fun n1 ->
    eval_expr e2 >>=
    int_of_numVal >>= fun n2 ->
    if n2==0
    then error "Division by zero"
    else return @@ NumVal (n1/n2)
  
  
  
  | Let(id,def,body) ->
    eval_expr def >>= 
    extend_env id >>+
    eval_expr body 
  
  
  | ITE(e1,e2,e3) ->
    eval_expr e1 >>=
    bool_of_boolVal >>= fun b ->
    if b 
    then eval_expr e2
    else eval_expr e3
  | IsZero(e) ->
    eval_expr e >>=
    int_of_numVal >>= fun n ->
    return @@ BoolVal (n = 0)
  
    | Proc(id,e)  ->
    lookup_env >>= fun en ->
    return (ProcVal(id,e,en))
  
    | App(e1,e2)  -> 
    eval_expr e1 >>= fun v1 ->
    eval_expr e2 >>= fun v2 ->
    apply_proc v1 v2 


  | Abs(e1) ->
    eval_expr e1 >>=
    int_of_numVal >>= fun n ->
    return @@ NumVal (abs(n))


  | EmptyList ->
    return @@ ListVal []


  | Cons(e1, e2) ->
    eval_expr e1 >>= fun ele ->
    eval_expr e2 >>= 
    list_of_listVal >>= fun l ->
    return @@ ListVal (ele :: l)
    
  | Empty(e) ->
    eval_expr e >>= list_of_listVal >>= empty' 


  | Hd(e) ->
    eval_expr e >>= list_of_listVal >>= hd' 

  | Tl(e) ->
    eval_expr e >>= list_of_listVal >>= tl' 

  
  | EmptyTree ->
    return @@ TreeVal Empty
  
  | Node(e1,lte,rte) ->
    let data = eval_expr e1 >>* bt'  in 
    let lt = eval_expr lte >>= tree_of_treeVal in 
    let rt = eval_expr rte >>= tree_of_treeVal in 
    lookup_env >>= fun en ->
    return @@ TreeVal(Node(( data en , b3t (lt en), b3t (rt en))))
  


  | CaseT(target,emptycase,id1,id2,id3,nodecase) ->
    lookup_env >>= fun en ->
      match eval_expr target en with 
      | Ok TreeVal Empty -> eval_expr emptycase 
      | Ok TreeVal Node( data,lt,rt) -> 
        let stage1 =  extend_env id1 data in 
        let stage2 =  extend_env id2 (TreeVal lt)  in
        let stage3 =  extend_env id3 (TreeVal rt) in
           (( stage1 >>+ stage2 ) >>+ stage3) >>+ (eval_expr nodecase)
        

and
  eval_prog (AProg e) = eval_expr e

(***********************************************************************)
(* Everything above this is essentially the same as we saw in lecture. *)
(***********************************************************************)

(* Parse a string into an ast *)

let parse s =
  let lexbuf = Lexing.from_string s in
  let ast = Parser.prog Lexer.read lexbuf in
  ast

let lexer s =
  let lexbuf = Lexing.from_string s
  in Lexer.read lexbuf 


(* Interpret an expression *)
let interp (e:string) : exp_val result =
  let c = e |> parse |> eval_prog
  in run c
