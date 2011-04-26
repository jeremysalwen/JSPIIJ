type strlen = record
s:string;
l:integer;
end;


procedure printdouble(x:real);
begin
writeln('double'#10);
end;

procedure printdouble(x:integer);
begin
writeln('int'#10,x,'lol'#10);
end;

var doobie:strlen;
begin
	doobie.s:='hello';
	doobie.l:=5;
    printdouble(doobie.l);
   writeln(doobie.s[2]);
end.
