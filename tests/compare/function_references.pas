var
    b: integer;

procedure setvalue(var a: integer);
begin
    a := 22;
end;

begin
    setvalue(b);
    write(b);
end.