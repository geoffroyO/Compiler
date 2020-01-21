parser grammar DecaParser;

options {
    // Default language but name it anyway
    //
    language  = Java;

    // Use a superclass to implement all helper
    // methods, instance variables and overrides
    // of ANTLR default methods, such as error
    // handling.
    //
    superClass = AbstractDecaParser;

    // Use the vocabulary generated by the accompanying
    // lexer. Maven knows how to work out the relationship
    // between the lexer and parser and will build the
    // lexer before the parser. It will also rebuild the
    // parser if the lexer changes.
    //
    tokenVocab = DecaLexer;

}

// which packages should be imported?
@header {
    import fr.ensimag.deca.tree.*;
    import java.io.PrintStream;
    import fr.ensimag.deca.tools.SymbolTable;
    import fr.ensimag.deca.syntax.*;
    import java.util.*;
}

@members {
    @Override
    protected AbstractProgram parseProgram() {
        return prog().tree;
    }
}

prog returns[AbstractProgram tree]
    : list_classes main EOF {
    		// System.out.println("### main 1 !!!###");
            assert($list_classes.tree != null);
            // System.out.println("### main 2 !!!###");
            assert($main.tree != null);
            // System.out.println("### main 3 !!!###");

            $tree = new Program($list_classes.tree, $main.tree);
            setLocation($tree, $list_classes.start);
        }
    ;

main returns[AbstractMain tree]
    : /* epsilon */ {
            $tree = new EmptyMain();
        }
    | block {
            assert($block.decls != null);
            assert($block.insts != null);
            $tree = new Main($block.decls, $block.insts);
            setLocation($tree, $block.start);
        }
    ;

block returns[ListDeclVar decls, ListInst insts]
    : OBRACE list_decl list_inst CBRACE {
            assert($list_decl.tree != null);
            assert($list_inst.tree != null);
            $decls = $list_decl.tree;
            $insts = $list_inst.tree;
        }
    ;

list_decl returns[ListDeclVar tree]
@init   {
            $tree = new ListDeclVar();
            /* il n'y a pas de setLocation() ici */
        }
    : decl_var_set[$tree]*
    ;

decl_var_set[ListDeclVar l]
    : type list_decl_var[$l,$type.tree] SEMI
    ;

list_decl_var[ListDeclVar l, AbstractIdentifier t]

    : dv1=decl_var[$t] {
    	assert($dv1.tree != null);
        $l.add($dv1.tree);
        } (COMMA dv2=decl_var[$t] {
        	assert($dv2.tree != null);
        	$l.add($dv2.tree);
        }
      )*
    ;

decl_var[AbstractIdentifier t] returns[AbstractDeclVar tree]
@init   {
        	AbstractInitialization initialization;
        }
    : i=ident {
        	initialization = new NoInitialization();
        }
      (EQUALS e=expr {
      		assert($e.tree != null);
            initialization = new Initialization($e.tree);
            setLocation(initialization, $EQUALS);
        }
      )? {
           	$tree = new DeclVar(t, $i.tree, initialization);
           	setLocation($tree, $i.start);      			

			
        }
    ;

list_inst returns[ListInst tree]
@init {
	$tree  = new ListInst();
}
    : (inst {
    		assert($inst.tree != null);
    		$tree.add($inst.tree);
        }
      )*
    ;

inst returns[AbstractInst tree]
    : e1=expr SEMI {
            assert($e1.tree != null);
            $tree = $e1.tree;
        }
    | SEMI {
        }
    | PRINT OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Print(false, $list_expr.tree);
            setLocation($tree, $PRINT);
        }
    | PRINTLN OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
			$tree = new Println(false,$list_expr.tree);
			setLocation($tree, $PRINTLN);
        }
    | PRINTX OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Print(true, $list_expr.tree);
            setLocation($tree, $PRINTX);            
        }
    | PRINTLNX OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Println(true, $list_expr.tree);
            setLocation($tree, $PRINTLNX);  
        }
    | if_then_else {
            assert($if_then_else.tree != null);
            $tree = $if_then_else.tree;
        }
    | WHILE OPARENT condition=expr CPARENT OBRACE body=list_inst CBRACE {
            assert($condition.tree != null);
            assert($body.tree != null);
            $tree = new While($expr.tree, $list_inst.tree);
            setLocation($tree, $WHILE);  
        }
    | RETURN expr SEMI {
            assert($expr.tree != null);
            $tree = new Return($expr.tree);
            setLocation($tree, $RETURN); 
        }
    ;

if_then_else returns[IfThenElse tree]
@init {
            ListInst else1 = new ListInst();
            ListInst else2 = new ListInst();
        
}
    : if1=IF OPARENT condition=expr CPARENT OBRACE li_if=list_inst CBRACE {
    		assert($condition.tree != null);
    		assert($li_if.tree != null);
            $tree = new IfThenElse($condition.tree, $li_if.tree, else1);
            setLocation($tree, $if1);
            
        }
      (ELSE elsif=IF OPARENT elsif_cond=expr CPARENT OBRACE elsif_li=list_inst CBRACE {
			
			assert($elsif_cond.tree != null);
			assert($elsif_li.tree != null);
            AbstractInst n = new IfThenElse($elsif_cond.tree, $elsif_li.tree, else2);
            setLocation(n, $ELSE);
            else1.add(n);
            else1 = else2;
            else2 = new ListInst();
            
        }
      )*
      (ELSE OBRACE li_else=list_inst CBRACE {
      		assert($li_else.tree != null);
      	    else1.addAll($li_else.tree);     	       
        }
      )?
    ;

list_expr returns[ListExpr tree]
@init   {
	$tree = new ListExpr();
        }
    : (e1=expr {
    	assert($e1.tree != null);
    	$tree.add($e1.tree);
        }
       (COMMA e2=expr {
       		assert($e2.tree != null);
       		$tree.add($e2.tree);
        }
       )* )?
    ;

expr returns[AbstractExpr tree]
    : assign_expr {
    		// System.out.println("### assign_expr 1 ###");
            assert($assign_expr.tree != null);
            // System.out.println("### assign_expr 2 ###");
            $tree = $assign_expr.tree;
        }
    ;

assign_expr returns[AbstractExpr tree]
    : e=or_expr (
        /* condition: expression e must be a "LVALUE" */ {
            if (! ($e.tree instanceof AbstractLValue)) {
                throw new InvalidLValue(this, $ctx);
            }
        }
        EQUALS e2=assign_expr {
        	// System.out.println("### or_expr 1 ###");
            assert($e.tree != null);
            assert($e2.tree != null);
			$tree = new Assign((AbstractLValue)$e.tree, $e2.tree);
			setLocation($tree,$EQUALS);
			// System.out.println("### or_expr 2 ###");
        }
      | /* epsilon */ {
      		// System.out.println("### or_expr EPSILON ###");
            assert($e.tree != null);
            $tree = $e.tree;
        }
      )
    ;

or_expr returns[AbstractExpr tree]
    : e=and_expr {
    		// System.out.println("### and_expr 1 ###");
            assert($e.tree != null);
            // System.out.println("### and_expr 2 ###");
            $tree = $e.tree;
        }
    | e1=or_expr OR e2=and_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);

            $tree = new Or($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
       }
    ;

and_expr returns[AbstractExpr tree]
    : e=eq_neq_expr {
    		// System.out.println("### eq_neq_expr 1 ###");
            assert($e.tree != null);
            // System.out.println("### eq_neq_expr 2 ###");
            $tree = $e.tree;
        }
    |  e1=and_expr AND e2=eq_neq_expr {
            assert($e1.tree != null);                         
            assert($e2.tree != null);
            $tree = new And($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    ;

eq_neq_expr returns[AbstractExpr tree]
    : e=inequality_expr {
    		// System.out.println("### inequality_expr 1 ###");
            assert($e.tree != null);
            // System.out.println("### inequality_expr 2 ###");
            $tree = $e.tree;
        }
    | e1=eq_neq_expr EQEQ e2=inequality_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Equals($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    | e1=eq_neq_expr NEQ e2=inequality_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new NotEquals($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    ;

inequality_expr returns[AbstractExpr tree]
    : e=sum_expr {
    		// System.out.println("### sum_expr 1 ###");
            assert($e.tree != null);
            // System.out.println("### sum_expr 2 ###");
            $tree = $e.tree;
        }
    | e1=inequality_expr LEQ e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new LowerOrEqual($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);

        }
    | e1=inequality_expr GEQ e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new GreaterOrEqual($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    | e1=inequality_expr GT e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Greater($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);

        }
    | e1=inequality_expr LT e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Lower($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);  
        }
    | e1=inequality_expr INSTANCEOF type {
            assert($e1.tree != null);
            assert($type.tree != null);
            /* 
            $tree = new IsInstanceOf($e1.tree, $type.tree);
            setLocation($tree, $e1.start);
			 */			 
        }
    ;

sum_expr returns[AbstractExpr tree]
    : e=mult_expr {
    		// System.out.println("### mult_expr 1 ###");
            assert($e.tree != null);
            // System.out.println("### mult_expr 2 ###");
            $tree = $e.tree;
        }
    | e1=sum_expr PLUS e2=mult_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Plus($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    | e1=sum_expr MINUS e2=mult_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Minus($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    ;

mult_expr returns[AbstractExpr tree]
    : e=unary_expr {
    		// System.out.println("### unary_expr ###");
            assert($e.tree != null);
            $tree = $e.tree;
        }
    | e1=mult_expr TIMES e2=unary_expr {
            assert($e1.tree != null);                                         
            assert($e2.tree != null);
            $tree = new Multiply($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    | e1=mult_expr SLASH e2=unary_expr {
            assert($e1.tree != null);                                         
            assert($e2.tree != null);
            $tree = new Divide($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    | e1=mult_expr PERCENT e2=unary_expr {
            assert($e1.tree != null);                                                                          
            assert($e2.tree != null);
            $tree = new Modulo($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    ;

unary_expr returns[AbstractExpr tree]
    : op=MINUS e=unary_expr {
            assert($e.tree != null);
            $tree = new UnaryMinus($e.tree);
            setLocation($tree, $op);
        }
    | op=EXCLAM e=unary_expr {
            assert($e.tree != null);
            $tree = new Not($e.tree);
            setLocation($tree, $op);
        }
    | select_expr {
    		// System.out.println("### select_expr ###");
            assert($select_expr.tree != null);
            $tree = $select_expr.tree;
        }
    ;

select_expr returns[AbstractExpr tree]
    : e=primary_expr {
    		// System.out.println("### primary_expr ###");
            assert($e.tree != null);
            $tree = $primary_expr.tree;
        }
    | e1=select_expr DOT i=ident {
            assert($e1.tree != null);
            assert($i.tree != null);
            // TO DO
        }
        (o=OPARENT args=list_expr CPARENT {
            // we matched "e1.i(args)"
            assert($args.tree != null);
            // TO DO

        }
        | /* epsilon */ {
            // we matched "e.i"
            // TO DO
        }
        )
    ;

primary_expr returns[AbstractExpr tree]
    : ident {
            assert($ident.tree != null);
            $tree = $ident.tree;
        }
    | m=ident OPARENT args=list_expr CPARENT {
            assert($args.tree != null);
            assert($m.tree != null);
            // TO DO
            
        }
    | OPARENT expr CPARENT {
            assert($expr.tree != null);
            $tree = $expr.tree;
        }
    | READINT OPARENT CPARENT {
    		$tree = new ReadInt();
    		setLocation($tree, $READINT);
        }
    | READFLOAT OPARENT CPARENT {
    		$tree = new ReadFloat();
    		setLocation($tree, $READFLOAT);
        }
    | NEW ident OPARENT CPARENT {
            assert($ident.tree != null);
            $tree = new New($ident.tree);
            setLocation($tree, $NEW);
        }
    | cast=OPARENT type CPARENT OPARENT expr CPARENT {
            assert($type.tree != null);
            assert($expr.tree != null);
            /*
            $tree = new Cast($type.tree, $expr.tree);
            setLocation($tree, $cast);
             */
        }
    | literal {
    		// System.out.println("### literal ###");
            assert($literal.tree != null);
            $tree = $literal.tree;
        }
    ;

type returns[AbstractIdentifier tree]
    : ident {
    		// System.out.println("### ident ###");
            assert($ident.tree != null);
            $tree = $ident.tree;
        }


    ;

literal returns[AbstractExpr tree]
    : i=INT {
    		$tree = new IntLiteral(Integer.parseInt($i.text));
    		setLocation($tree, $i);
        }
    | fd=FLOAT {
    		$tree = new FloatLiteral(Float.parseFloat($fd.text));
    		setLocation($tree, $fd);
        }
    | str=STRING {
   			// Method substring to remove " " from the STRING TOKEN
    		$tree = new StringLiteral( $str.text.substring(1,$str.text.length()-1) );
			setLocation($tree, $str);
        }
    | TRUE {
    		$tree = new BooleanLiteral(true);
    		setLocation($tree, $TRUE);
        }
    | FALSE {
    		$tree = new BooleanLiteral(false);
    		setLocation($tree, $FALSE);
      	}
    | THIS {
    		/*
    		$tree = new This();
    		setLocation($tree, $THIS); 
    		 */
        }
    | NULL {
    		$tree = new Null();
    		setLocation($tree, $NULL);
        }
    ;

ident returns[AbstractIdentifier tree]
    : IDENT {
           $tree = new Identifier(getDecacCompiler().getSymbols().create($IDENT.text));
           setLocation($tree, $IDENT);
        }
    ;

/****     Class related rules     ****/

list_classes returns[ListDeclClass tree]
	@init {
			// System.out.println("list classes");	
            $tree = new ListDeclClass();
	}
	: (c1=class_decl {
			// System.out.println("list classes");
			assert($c1.tree != null);
			$tree.add($c1.tree);
		}
	)*
	;

class_decl returns[DeclClass tree]
    : CLASS name=ident superclass=class_extension OBRACE class_body CBRACE {
    		assert($name.tree != null);
    		assert($superclass.tree != null);
    		assert($class_body.fields != null);
    		assert($class_body.methods != null);
    		$tree = new DeclClass($name.tree, $superclass.tree, $class_body.fields, $class_body.methods);
    		setLocation($tree, $CLASS);
    		setLocation($superclass.tree, $superclass.start);
        }
    ;

class_extension returns[AbstractIdentifier tree]
    : EXTENDS ident {
    		assert($ident.tree != null);
    		$tree = $ident.tree;
    		setLocation($tree, $ident.start);
        }
    | /* epsilon */ {
    		$tree = new Identifier(getDecacCompiler().getSymbols().create("Object"));
        }
    ;

class_body returns[ListDeclMethod methods, ListDeclField fields]
	@init {
		$methods = new ListDeclMethod();
		$fields = new ListDeclField();
		
	}
    : (m=decl_method {
    		$methods.add($m.tree);
        }
      | decl_field_set[$fields]
      )*
    ;

decl_field_set[ListDeclField fields]
    : v=visibility t=type list_decl_field[fields, $v.visib, $t.tree] SEMI
    ;

visibility returns[Visibility visib]
    : /* epsilon */ {
    	$visib = Visibility.PUBLIC;
        }
    | PROTECTED {
    	$visib = Visibility.PROTECTED;
        }
    ;
    
list_decl_field[ListDeclField fields, Visibility visib, AbstractIdentifier t]

    : dv1=decl_field[visib, t] {
    		assert($dv1.tree != null);
    		fields.add($dv1.tree);
    	}
        (COMMA dv2=decl_field[visib, t] {
        	assert($dv2.tree != null);
        	fields.add($dv2.tree);
        }
      )*
    ;

decl_field[Visibility visib, AbstractIdentifier t] returns[AbstractDeclField tree]
	@init {
			AbstractInitialization initialization;
	}
    : i=ident {
    		initialization = new NoInitialization();
    		
        }
      (EQUALS e=expr {
      		assert($e.tree != null);
      		initialization = new Initialization($e.tree);
      		setLocation(initialization, $EQUALS);
        }
      )? {
      		assert($i.tree != null);
      		$tree = new DeclField(visib, t, $i.tree, initialization);
      		setLocation($tree, $i.start);
        }
    ;


decl_method returns[AbstractDeclMethod tree]
	@init {
		AbstractMethodBody body;
	}
    : type ident OPARENT params=list_params CPARENT (block {
    		assert($block.decls != null);
    		assert($block.insts != null);
    		body = new MethodBody($block.decls, $block.insts);
    		setLocation(body, $block.start);
    		
        }
      | ASM OPARENT code=multi_line_string CPARENT SEMI {
      		assert($code.text != null);
      		body = new MethodAsmBody($code.text, $code.location);
      		setLocation(body, $ASM);
        }
      ) {
      		assert($type.tree != null);
      		assert($ident.tree != null);
      		assert($params.tree != null);
      		$tree = new DeclMethod($type.tree, $ident.tree, $params.tree, body);
      		setLocation($tree, $type.start);
        }
    ;

list_params returns[ListDeclParam tree]
	@init {
			$tree = new ListDeclParam();
	}
    : (p1=param {
    		assert($p1.tree != null);
    		$tree.add($p1.tree);
        } (COMMA p2=param {
        	assert($p2.tree != null);
        	$tree.add($p2.tree);
        }
      )*)?
    ;
    
multi_line_string returns[String text, Location location]
    : s=STRING {
            $text = $s.text;
            $location = tokenLocation($s);
        }
    | s=MULTI_LINE_STRING {
            $text = $s.text;
            $location = tokenLocation($s);
        }
    ;

param returns[AbstractDeclParam tree]
    : type ident {
    		assert($type.tree != null);
    		assert($ident.tree != null);
    		$tree = new DeclParam($type.tree, $ident.tree);
    		setLocation($tree, $type.start);  		
        }
    ;

