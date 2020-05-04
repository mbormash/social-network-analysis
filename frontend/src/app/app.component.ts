import {Component} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  public isInputtingData = false;
  public isShowingTable = false;
  public numberOfUsers;
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

    if (AppComponent.isNumberMoreThan2(inputValue)) {
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

  predictCommunications() {
    let cells = document.getElementsByClassName("matrix-cell");

    let relationsMatrix = [];
    let row = [];
    let counter = 0;
    for (let i = 0; i < cells.length; i++) {
      row.push(cells.item(i).textContent);
      counter++;

      if (counter === this.numberOfUsers) {
        counter = 0;
        relationsMatrix.push(row);
        row = [];
      }
    }

    if (AppComponent.validateMatrix(relationsMatrix)) {
      console.log(JSON.stringify(relationsMatrix));
    }
  }

  private static validateMatrix(matrix: Array<Array<any>>): boolean {
    for (let i = 0; i < matrix.length; i++) {
      for (let j = 0; j < matrix[i].length; j++) {
        let element = matrix[i][j];
        if (element === "") {
          continue;
        }

        if (!AppComponent.isNumber(element)) {
          return false;
        }

        let number = parseInt(element);
        if (number !== 0 && number !== 1 && number !== -1) {
          return false;
        }
      }
    }

    return true;
  }


  private static isNumberMoreThan2(number: string): boolean {
    return AppComponent.isNumber(number) && parseInt(number) >= 3;
  }

  private static isNumber(number: string): boolean {
    return /^-?[\d.]+(?:e-?\d+)?$/.test(number);
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
