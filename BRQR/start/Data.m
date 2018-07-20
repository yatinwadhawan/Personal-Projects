classdef Data
    %INPUT Summary of this class goes here
    %   Detailed explanation goes here
    
    properties
        defcov
        defucov
        attaucov
        attacov
        time
    end
    
    methods
        function obj = Data(val)
            rng(0,'twister');
            a = 0;
            b = 100;
            c = -100;
            d = 0;
            x = 1;
            y = 20;
            for i=1:val
                obj.defcov( i ) = (b-a).*rand(1,1) + a;
                obj.attaucov( i ) = (b-a).*rand(1,1) + a;
                obj.defucov( i ) = (d-c).*rand(1,1) + c;
                obj.attacov( i ) = (d-c).*rand(1,1) + c;
                obj.time( i ) = (y-x).*rand(1,1) + x;
            end
        end
    end
    
end

