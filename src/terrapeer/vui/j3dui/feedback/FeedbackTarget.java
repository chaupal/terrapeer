package terrapeer.vui.j3dui.feedback;

/**
This interface serves as a target for feedback state events.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/
public interface FeedbackTarget {
	
	// public interface =========================================

	/**
	Called when the feedback status state changes.
	@param status New status state (Feedback.STATUS_???).
	*/
	public void setFeedbackStatus(int status);

	/**
	Called when the feedback select state changes.
	@param select New selection state (Feedback.SELECT_???).
	*/
	public void setFeedbackSelect(int select);

	/**
	Called when the feedback action state changes.
	@param action New action state (Feedback.ACTION_???).
	*/
	public void setFeedbackAction(int action);
	
}