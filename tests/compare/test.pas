type strlen = record
s:string;
l:integer;
a: array of integer;
end;


procedure printdouble(x:real);
begin
writeln('double'#10);
end;

procedure printdouble(x:integer);
begin
writeln('int'#10,x,'lol'#10);
end;

var doobie:strlen; i:integer;
begin
    doobie.s:='hello';
    doobie.l:=5;
    setlength(doobie.a,5);
    for i:=0 to 4 do begin
      doobie.a[i]:=i;
    end;
    writeln(doobie.a[2]);
    printdouble(doobie.l);
   writeln(doobie.s[2]);
end.
