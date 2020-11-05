grammar OLAP;
import COOL;

operator 
  : drill
  | rollup
  | sad
  | add
  | remove
  | replace;

replace: 'REPLACE' replace_spec;
replace_spec
  : fromMea=mc   'ACCESSORY' toMea=mc   #replaceMea
  | fromAtt=ATTR 'ACCESSORY' toAtt=ATTR #replaceAttr
  | fromSel=ssc  'ACCESSORY' toSel=ssc  #replaceSSC
//  : fromMea=mc   'WITH' toMea=mc   #replaceMea
//  | fromAtt=ATTR 'WITH' toAtt=ATTR #replaceAttr
//  | fromSel=ssc  'WITH' toSel=ssc  #replaceSSC
  ;

remove: 'DROP' remove_spec;
remove_spec
  : mea=mc   #removeMea
  | att=ATTR #removeAttr
  | sel=ssc  #removeSSC
  ;

add: 'ADD' add_spec;
add_spec
  : mea=mc   #addMea
  | att=ATTR #addAttr
  | sel=ssc  #addSSC
  ;

sad: 'SAD' sel=ssc;

// rollup: 'ROLLUP' fromAtt=ATTR ('TO' toAtt=ATTR);
// drill:  'DRILL'  fromAtt=ATTR ('TO' toAtt=ATTR);
rollup: 'ROLLUP' fromAtt=ATTR ('ACCESSORY' toAtt=ATTR)?;
drill:  'DRILL'  fromAtt=ATTR ('ACCESSORY' toAtt=ATTR)?;