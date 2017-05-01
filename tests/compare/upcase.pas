var
    c: char;
    s: string;
begin
    for c:='a' to 'c' do
        write(upcase(c));
    writeln;
    s := 'abc';
    write(upcase(s[1]));
end.