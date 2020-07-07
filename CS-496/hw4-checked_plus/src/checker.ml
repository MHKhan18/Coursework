(* 

Name : Mohammad Khan
Date: 7/01/20
Pledge: I pledge my honor that I have abided by the Stevens Honor System.

*)


open Ast
open ReM
open Dst


let rec type_of_expr : expr -> texpr tea_result = function 
  | Int _n -> return IntType
  | Var id -> apply_tenv id
  | IsZero(e) ->
    type_of_expr e >>= fun t ->
    if t=IntType
    then return BoolType
    else error "isZero: expected argument of type int"
  | Add(e1,e2) | Sub(e1,e2) | Mul(e1,e2)| Div(e1,e2) ->
    type_of_expr e1 >>= fun t1 ->
    type_of_expr e2 >>= fun t2 ->
    if (t1=IntType && t2=IntType)
    then return IntType
    else error "arith: arguments must be ints"
  | ITE(e1,e2,e3) ->
    type_of_expr e1 >>= fun t1 ->
    type_of_expr e2 >>= fun t2 ->
    type_of_expr e3 >>= fun t3 ->
    if (t1=BoolType && t2=t3)
    then return t2
    else error "ITE: condition not boolean or types of then and else do not match"
  | Let(id,e,body) ->
    type_of_expr e >>= fun t ->
    extend_tenv id t >>+
    type_of_expr body
  | Proc(var,t1,e) ->
    extend_tenv var t1 >>+
    type_of_expr e >>= fun t2 ->
    return @@ FuncType(t1,t2)
  | App(e1,e2) ->
    type_of_expr e1 >>=
    pair_of_funcType "app: " >>= fun (t1,t2) ->
    type_of_expr e2 >>= fun t3 ->
    if t1=t3
    then return t2
    else error "app: type of argument incorrect"
  | Pair(e1,e2) ->
    type_of_expr e1 >>= fun t1 ->
    type_of_expr e2 >>= fun t2 ->
    return @@ PairType(t1,t2)
  | Unpair(id1,id2,e1,e2) ->
    type_of_expr e1 >>=
    pair_of_pairType "unpair: " >>= fun (t1,t2) ->
    extend_tenv id1 t1 >>+
    extend_tenv id2 t2 >>+
    type_of_expr e2  
  | Letrec(id,param,tParam,tRes,body,target) ->
    extend_tenv id (FuncType(tParam,tRes)) >>+
    (extend_tenv param tParam >>+
     type_of_expr body >>= fun t ->
     if t=tRes 
     then type_of_expr target
     else error
         "LetRec: Type of recursive function does not match declaration")

  | Unit -> return UnitType
  (* references *)
  | BeginEnd(es) ->
    List.fold_left (fun r e -> r >>= fun _ -> type_of_expr e) (return UnitType) es 
  | NewRef(e) ->
    type_of_expr e >>= fun t ->
    return @@ RefType(t) 
  | DeRef(e) ->
    type_of_expr e >>= 
    arg_of_refType "deref: " >>= fun (t) ->
    return @@ t 
  | SetRef(e1,e2) ->
    type_of_expr e1 >>= 
    arg_of_refType "setref: " >>= fun (t1) ->
    type_of_expr e2 >>= fun t2 ->
    if (t1=t2)
    then return UnitType
    else error "SetRef: Type of new expression does not match previous expression"
    
 
  (* lists *)
  | EmptyList(t) ->
    return @@ ListType t 

  | Cons(h, t) ->
    type_of_expr t >>= 
    arg_of_listType "cons: " >>= fun (tt) ->
    type_of_expr h >>= fun th ->
    if (tt=th)
    then return  @@ ListType tt
    else error "cons : type of head and tail do not match "
    
    
  | Null(e) ->
    type_of_expr e >>= 
    arg_of_listType "null: " >>= fun (_) ->
    return BoolType
       
  | Hd(e) ->
    type_of_expr e >>= 
    arg_of_listType "hd: " >>= fun (t) ->
    return t 
    
  | Tl(e) ->
    type_of_expr e >>= 
    arg_of_listType "tl: " >>= fun (t) ->
    return  @@ ListType t
      

  (* trees *)
  | EmptyTree(t) ->
    return @@ TreeType t 
  
  | Node(de, le, re) ->
  type_of_expr le >>= 
  arg_of_treeType "node: " >>= fun (t1) ->
  type_of_expr re >>= 
  arg_of_treeType "node: " >>= fun (t2) ->
  type_of_expr de >>= fun (t3) ->
  if ( t1=t2 && t2=t3 )
  then return @@ TreeType t1 
  else error "node : type of data ,  lt and rt do not match "
    
  | NullT(t) ->
    type_of_expr t >>= 
    arg_of_treeType "nullt: " >>= fun (_) ->
    return BoolType
  
  | GetData(t) ->
    type_of_expr t >>= 
    arg_of_treeType "getdata: " >>= fun (t1) ->
    return t1
    
  | GetLST(t) ->
    type_of_expr t >>= 
    arg_of_treeType "getlst: " >>= fun (t1) ->
    return @@ TreeType t1 
      
  | GetRST(t) ->
    type_of_expr t >>= 
    arg_of_treeType "getrst: " >>= fun (t1) ->
    return @@ TreeType t1 
      


  | Debug(_e) ->
    string_of_tenv >>= fun str ->
    print_endline str;
    error "Debug called!"
  | _ -> error "type_of_expr: implement"    



let parse s =
  let lexbuf = Lexing.from_string s in
  let ast = Parser.prog Lexer.read lexbuf in
  ast


(* Type-check an expression *)
let chk (e:string) : texpr result =
  let c = e |> parse |> type_of_expr
  in run_teac c

let chkpp (e:string) : string result =
  let c = e |> parse |> type_of_expr
  in run_teac (c >>= fun t -> return @@ Ast.string_of_texpr t)

