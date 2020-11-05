grammar COOL;
@header {
package it.unibo.antlr.gen;
}

init
  : GPSJ=gpsj               #full
  | GPSJ=gpsj UP+=unparsed+ #partial
;

unparsed : ssc | gc | gpsj;

gpsj
  : MC+=mc+ SC=sc GC=gc
  | MC+=mc+ GC=gc SC=sc
  | SC=sc MC+=mc+ GC=gc
  | SC=sc GC=gc MC+=mc+
  | GC=gc MC+=mc+ SC=sc
  | GC=gc SC=sc MC+=mc+
  | MC+=mc+ GC=gc
  | GC=gc MC+=mc+
  | MC+=mc+ SC=sc
  | SC=sc MC+=mc+
  | MC+=mc+;

mc
  : agg=AGG? mea=MEA      #mea
  | cnt='COUNT' fact=FACT #fact
;

sc: where='WHERE'? SSC=ssc;

ssc
  : unary=NOT left=ssc                                                    #unary
  | left=ssc binary=BINARY right=ssc                                      #binary
  | (attr1=ATTR cop=COP? val=VAL | val=VAL cop=COP? attr1=ATTR | val=VAL) #atom
;

gc : by='BY' attr+=ATTR+;

AGG    : 'AGG';
MEA    : 'MEA';
FACT   : 'FACT';
ATTR   : 'ATTR';
VAL    : 'VAL';
BINARY : 'AND' | 'OR';
NOT    : 'NOT';
COP    : 'COP';
// SELECT : 'SELECT';
WS     : [ \t\r\n]+ -> skip;
ERRCHAR : . ;