import {Component} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  public isInputtingData = false;
  public isShowingTable = false;
  public numberOfUsers = 20;
  public codeNames;

  generateArrayOfNumbers() {
    let codeName = "@";

    return Array(this.numberOfUsers).fill("").map(() => {
      codeName = AppComponent.generateCodeName(codeName);
      return codeName;
    });
  }

  changeInput() {
    this.isInputtingData = !this.isInputtingData;

    if (!this.isInputtingData) {
      this.isShowingTable = false;
    }
  }

  showTable() {
    let inputElement = document.getElementById("inputNumUsers");
    let inputValue = (<HTMLInputElement>inputElement).value;

    if (AppComponent.isNumberMoreThan3(inputValue)) {
      this.numberOfUsers = parseInt(inputValue);
      this.codeNames = this.generateArrayOfNumbers();

      this.isShowingTable = true;
    } else {
      let buttonElement = document.getElementById("showTable");

      inputElement.setAttribute("class", "form-control blinking");
      buttonElement.setAttribute("class", "btn btn-outline-secondary blinking");

        setTimeout(() => {
          inputElement.setAttribute("class", "form-control");
          buttonElement.setAttribute("class", "btn btn-outline-secondary");
        }, 2000);
    }
  }

  private static isNumberMoreThan3(number: string): boolean {
    if (/^-?[\d.]+(?:e-?\d+)?$/.test(number)) {
      return parseInt(number) >= 3;
    }

    return false;
  }

  private static generateCodeName(codeName) {
    if (codeName.endsWith("Z")) {
      codeName = AppComponent.incrementCodeNameLength(codeName);
    } else {
      codeName = AppComponent.incrementLastChar(codeName);
    }

    return codeName;
  }

  private static incrementCodeNameLength(codeName) {
    let length = codeName.length;
    let newCodeName = "";

    for (let i = 0; i < length + 1; i++) {
      newCodeName += "A";
    }

    return newCodeName;
  }

  private static incrementLastChar(codeName) {
    let lastChar = codeName.charAt(codeName.length - 1);
    lastChar = String.fromCharCode(lastChar.charCodeAt(0) + 1);

    return codeName.substring(0, codeName.length - 1) + lastChar;
  }
}
