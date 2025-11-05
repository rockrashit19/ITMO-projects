#include "lab1.h"

#include "gpio.h"

void GPIO_CustomWrite(GPIO_TypeDef* port, uint16_t pin, uint8_t state){
	if (state) port->BSRR = pin;
	else port->BSRR = (uint32_t)pin << 16U;
}

uint8_t GPIO_CustomRead(GPIO_TypeDef* port, uint16_t pin){
	return ((port->IDR & pin) != 0U) ? 1U : 0U;
}

int8_t buttonPressed = 0;


void proc1() {

	static uint32_t holdStart = 0;
	static int8_t isHolding = 0;

	if (GPIO_CustomRead(GPIOC, GPIO_PIN_15) == GPIO_PIN_SET) {
		uint32_t now = HAL_GetTick();
		if (holdStart > 0) {
			if (now - holdStart > 5) { // > 5ms
				buttonPressed = !isHolding;
				isHolding = 1;
			}
		} else {
			holdStart = now;
		}
	} else {
		holdStart = 0;
		isHolding = 0;
	}

}


typedef enum {
	COL_RED_MIN, 			// 2s
	COL_RED_REMAINING, 		// 6s
	COL_GREEN, 				// 2s
	COL_GREEN_BLINK_1_OFF, 	// 0.1s
	COL_GREEN_BLINK_1_ON, 	// 0.1s
	COL_GREEN_BLINK_2_OFF, 	// 0.1s
	COL_GREEN_BLINK_2_ON, 	// 0.1s
	COL_YELLOW 				// 1s
} State;


typedef enum {
	LED_NONE,
	LED_RED,
	LED_GREEN,
	LED_YELLOW
} LedColor;

void toggleLED(LedColor col) {
	switch (col) {
		case LED_GREEN: {
			GPIO_CustomWrite(GPIOD, GPIO_PIN_13, GPIO_PIN_SET);
			GPIO_CustomWrite(GPIOD, GPIO_PIN_14, GPIO_PIN_RESET);
			GPIO_CustomWrite(GPIOD, GPIO_PIN_15, GPIO_PIN_RESET);
			break;
		}
		case LED_YELLOW: {
			GPIO_CustomWrite(GPIOD, GPIO_PIN_13, GPIO_PIN_RESET);
			GPIO_CustomWrite(GPIOD, GPIO_PIN_14, GPIO_PIN_SET);
			GPIO_CustomWrite(GPIOD, GPIO_PIN_15, GPIO_PIN_RESET);
			break;
		}
		case LED_RED: {
			GPIO_CustomWrite(GPIOD, GPIO_PIN_13, GPIO_PIN_RESET);
			GPIO_CustomWrite(GPIOD, GPIO_PIN_14, GPIO_PIN_RESET);
			GPIO_CustomWrite(GPIOD, GPIO_PIN_15, GPIO_PIN_SET);
			break;
		}
		case LED_NONE: {
			GPIO_CustomWrite(GPIOD, GPIO_PIN_13 | GPIO_PIN_14 | GPIO_PIN_15, GPIO_PIN_RESET);
			break;
		}
		default: {
			GPIO_CustomWrite(GPIOD, GPIO_PIN_13 | GPIO_PIN_14 | GPIO_PIN_15, GPIO_PIN_RESET);
			break;
		}
	}
}


void proc2() {

	static State state = COL_YELLOW;
	static int8_t shoudToggleGreenASAP = 0;
	static uint32_t lastActionTime = 0;


	if (state != COL_GREEN && buttonPressed) shoudToggleGreenASAP = 1;

	uint32_t now = HAL_GetTick();

	switch (state) {
	    case COL_RED_MIN: { // 2000
	        if (now - lastActionTime > 2000) {
	            state = COL_RED_REMAINING;
	            lastActionTime = now;
	        }
	        break;
	    }
	    case COL_RED_REMAINING: { // 6000
	        if (now - lastActionTime > 6000 || shoudToggleGreenASAP) {
	        	shoudToggleGreenASAP = 0;
	            state = COL_GREEN;
	            lastActionTime = now;
	            toggleLED(LED_GREEN);
	        }
	        break;
	    }
	    case COL_GREEN: { // 2000
	        if (now - lastActionTime > 2000) {
	            state = COL_GREEN_BLINK_1_OFF;
	            lastActionTime = now;
	            toggleLED(LED_NONE);
	        }
	        break;
	    }
	    case COL_GREEN_BLINK_1_OFF: { // 100
	        if (now - lastActionTime > 100) {
	            state = COL_GREEN_BLINK_1_ON;
	            lastActionTime = now;
	            toggleLED(LED_GREEN);
	        }
	        break;
	    }
	    case COL_GREEN_BLINK_1_ON: { // 100
	        if (now - lastActionTime > 100) {
	            state = COL_GREEN_BLINK_2_OFF;
	            lastActionTime = now;
	            toggleLED(LED_NONE);
	        }
	        break;
	    }
	    case COL_GREEN_BLINK_2_OFF: { // 100
	        if (now - lastActionTime > 100) {
	            state = COL_GREEN_BLINK_2_ON;
	            lastActionTime = now;
	            toggleLED(LED_GREEN);
	        }
	        break;
	    }
	    case COL_GREEN_BLINK_2_ON: { // 100
	        if (now - lastActionTime > 100) {
	            state = COL_YELLOW;
	            lastActionTime = now;
	            toggleLED(LED_YELLOW);
	        }
	        break;
	    }
	    case COL_YELLOW: { // 1000
	        if (now - lastActionTime > 1000) {
	            state = COL_RED_MIN;
	            lastActionTime = now;
	            toggleLED(LED_RED);
	        }
	        break;
	    }
	}

}
