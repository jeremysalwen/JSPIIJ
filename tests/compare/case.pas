var
 num : integer = 0;

Begin
 while True do begin
  num := num + 1;
  case num of
   1: writeln('1'); //<<<
   2: writeln('2');
   3: writeln('3');
   4: writeln('4');
   10: break;
   else
   break;
  end;
 end;
End.