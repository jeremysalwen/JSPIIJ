type coords= record
x:integer;
y:integer;
end

var
i, target:integer;
r:real;
point:coords;
cow:array[0 .. 10] of integer;
begin
writeln('haha fools');
target:=10;
r:=10;
cow[0]:=1;
cow[1]:=1;
for i:=2 to 10 do
begin
	cow[i]:=cow[i-1]+cow[i-2];
	writeln(cow[i]);
end
end.