class Button
{
  boolean isClicked;
  int x, y, wtd, hgt;
  color originalColor, pressedColor;
  String buttonText;
  Button(int x, int y, int wtd, int hgt, color orgClr, color prsClr, String buttonText)
  {
    this.x = x;
    this.y = y;
    this.wtd = wtd;
    this.hgt = hgt;
    this.originalColor = orgClr;
    this.pressedColor = prsClr;
    this.buttonText = buttonText;
  }
  
  void displayButton()
  {
    if(onButton() && mousePressed)
    {
      fill(pressedColor);
      isClicked = true;
    }
    else
    {
      fill(originalColor);
      isClicked = false;
    }
    stroke(0);
    strokeWeight(1);
    rect(x, y, wtd, hgt);
    if(onButton() && mousePressed)
      fill(80);
    else
      fill(0);
    textAlign(CENTER,CENTER);
    textSize(hgt*4/5);
    text(buttonText, (2*x+wtd)/2, (2*y+hgt)/2);
  }
  
  boolean onButton()
  {
    if(mouseX >= x && mouseX <= x+wtd && mouseY >= y && mouseY <= y+hgt)
    {
      translX = 0;
      translY = 0;
      return true;
    }
    return false;
  }
}

void displayMenu()
{
  background(menuColor);
  translate(0, 0);
  for(int i = 0; i < nrOfLevels; ++i)
  {
    buttons[i].displayButton();
    if(buttons[i].isClicked)
    {
      whichWorld = i;
      playerX = playerSpawn[whichWorld][0];
      playerY = playerSpawn[whichWorld][1];
    }
  }
}
