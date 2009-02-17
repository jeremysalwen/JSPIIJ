var
i, target:integer;
r:real;
begin
writeln('haha fools');
target:=10;
r:=10;
for i:=1 to 10 do
begin
	if (r * r) > target then 
	r:=r/2;
	else r:=r*1.5;
	writeln(r);
end
end.