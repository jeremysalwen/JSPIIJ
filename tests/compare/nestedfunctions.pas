program nestedfunction;

function fibonacci(n: integer) : integer;
var
    tmp: integer;
function realfib(n1, n2, numleft: integer) : integer;
var
    tmp: integer;
begin
    if numleft = 0 then
        result := n2
    else begin
        tmp := n2;
        n2 := n1 + n2;
        n1 := n2;
        result := realfib(n1,n2,numleft - 1);
    end
end;
begin
    result := realfib(0,1,n);
end;

begin
    writeln(fibonacci(4));
end.
