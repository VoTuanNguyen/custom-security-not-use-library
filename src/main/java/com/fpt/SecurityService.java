package com.fpt;

import org.springframework.stereotype.Service;

@Service
public class SecurityService {
  
   public boolean hasAccess() {
	   System.out.println("OK, da vao!");
       return false;
   }
}
  